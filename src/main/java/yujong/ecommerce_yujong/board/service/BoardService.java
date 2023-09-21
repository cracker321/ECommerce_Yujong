package yujong.ecommerce_yujong.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yujong.ecommerce_yujong.board.dto.BoardPatchDto;
import yujong.ecommerce_yujong.board.dto.BoardPostDto;
import yujong.ecommerce_yujong.board.dto.BoardResponseDto;
import yujong.ecommerce_yujong.board.entity.Board;
import yujong.ecommerce_yujong.board.mapper.BoardMapper;
import yujong.ecommerce_yujong.board.repository.BoardRepository;
import yujong.ecommerce_yujong.global.exception.BusinessLogicException;
import yujong.ecommerce_yujong.global.exception.ExceptionCode;
import yujong.ecommerce_yujong.member.entity.Seller;
import yujong.ecommerce_yujong.member.service.SellerService;
import yujong.ecommerce_yujong.product.entity.Product;
import yujong.ecommerce_yujong.product.repository.ProductRepository;
import yujong.ecommerce_yujong.product.service.ProductService;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly=true)
@Service
public class BoardService { //완료!!

    private final SellerService sellerService;
    private final BoardMapper boardMapper;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final BoardRepository boardRepository;


//=============================================================================================================


    //[ 게시글 Board 등록 Create ]

    public BoardResponseDto createBoard(BoardPostDto boardPostDto) {

        //*****중요*****
        //- '메소드 BoardService.createBoard(...)'는, '컨트롤러 메소드 BoardService'에서 사용되는 메소드이기 때문에,
        //  '컨트롤러 메소드 BoardService'가 '@RequestBody BoardPostDto boardPostDto'로 받아 캐치해온
        //  'BoardPostDto 객체'를 사용할 수 있는 것이다!
        //- 당연히 '컨트롤러 메소드 BoardService'에서 사용되지 않으며 완전 무관한 어떤 다른 외부 Service의 내부 메소드,
        //  예를 들어, '메소드 CommentService.deleteComment()'에서는,
        //  당연히 BoardPostDto를 사용하지 못한다!! 당연...
        //- 다만, 컨트롤러 메소드 postBoard는 그 내부에 메소드 boardService.createBoard() 를 사용하는데,
        //  이 boardService.createBoard() 는 메소드 ProductService.createProduct 를 사용하고 있기 때문에,
        //  ProductService.createProduct() 에서는 당연히 그 내부에 BoardPostDto 객체를 사용할 수 있음.
        //  죽, 직, 간접적으로 연결되어 있는 다른 외부 클래스의 메소드에서는 BoardPostDto 객체를 사용할 수 있는 것이다!


        //순서1) < 'SellerService.findVerifiedSeller()':
        //        DB에 현재 존재하는 판매자 Seller 인지 여부를 확인하고, 존재한다면 그 판매자 Seller를 가져와서 반환해주고,
        //        아니라면, Optional로 처리해서 내가 지정한 사용자 정의 에러 ExceptionCode.MEMBER_NOT_FOUND 를 발생시켜줌. >
        Seller findSeller = sellerService.findVerifiedSeller(boardPostDto.getSellerId());


        //순서2) < 게시글 Board 내에 들어갈 DB에 이미 저장시킨 상품 Product에 필요한 상품 Product 그 자체의(자체에) 필요한 정보 입력 >
        //- 'productService.createProduct': 'DB에 등록 저장시키는 BoardPostDto 객체의 필드 구성에 맞게
        //   커스터마이징된 DB에 저장 등록시킬 상품 Product 엔티티 객체를 DB에 등록 저장시키고 그 저장시킨 Product를 꺼내와서 반환함.
        Product product = productService.createProduct(findSeller, boardPostDto);


        //순서3) < 위에서의 '판매자 Seller'와 '게시글 Board 내에 들어갈 상품 Product 자체 정보'를 바탕으로 이제
        //        '게시글 Board'를 DB에 등록 >
        Board board = boardMapper.boardPostDtoToBoard(boardPostDto);
        board.setSeller(findSeller);
        board.setProduct(product);
        product.setLeftStock(product.getStock());
        boardRepository.save(board); //DB에 게시글 Board 최종 등록 저장


        //순서4) < >
        BoardResponseDto boardResponseDto = boardMapper.productToBoardResponseDto(product, board);

        return boardResponseDto;


    }

//=============================================================================================================


    //[ 게시글 Board 수정 Update ]

    public BoardResponseDto updateBoard(long boardId, BoardPatchDto boardPatchDto) {


        //순서1)
        // < 'BoardService.findVerifiedBoard()':
        //   DB에 현재 존재하는 게시글 Board 인지 여부를 확인하고, 존재한다면 그 게시글 Board 를 가져와서 반환해주고,
        //   아니라면, Optional로 처리해서 내가 지정한 사용자 정의 에러 ExceptionCode.BOARD_NOT_FOUND 를 발생시켜줌. >
        Board findBoard = findVerifiedBoard(boardPatchDto.getBoardId());


        //순서2) 상품 Product 업데이트
        Product updatedProduct = productService.updateProduct(findBoard, boardPatchDto);

        //[ Optional.ofNullalbe(인수).ifPresent(인수<> action) ]


        //- 'Optional.ofNullable(인수)'
        //   : DB를 조회해본 후에, 주어진 인수에 해당하는 데이터가 DB에 없는 경우(null)라면, Optional.empty() 객체를 반환하고,
        //     이 때 반환된 Optional.empty() 객체를 예외 처리 해주기 위해,
        //     이 반환된 객체를 참조하는 변수에 대해 그 아래 줄에 반드시 orElseThrow를 작성해줘야 한다!
        //     만약 DB를 조회해본 후, 주어진 인수에 해당하는 데이터가 DB에 있는 경우라면,
        //     당연히 이 때 주어진 인수를 감싸고 있는 Optional 객체를 반환해줌.
        //     그리고 이 때도, 반환된 Optional 객체에서 Optional 을 벗겨내기 위해 orElseThrow 를 위에서와 같이 작성해줘야 함.


        // - 'ifPresent(사용자 정의 변수명(아무거나 해도 되. a, b, c 등등) -> action)'
        //   : Optional.ofNullable(인수)에 이어져서 호출되는 Optional 클래스의 내장 메소드이며,
        //     Optional.ofNullable(인수)의 '인수'가 DB에 존재하는 경우 그 인수를 조회해서 가져오고,
        //     그 db로부터 가져온 인수를 감싸고 있는 Optional 객체가 반환되는데,
        //     그 db로부터 가져온 인수를 감싸고 있는 Optional 객체를 인수로 받아들이고, 여기서 '사용자 정의 변수명'이
        //     그 db로부터 가져온 인수를 감싸고 있는 Optional 객체를 담고(=참조하고),
        //     그 변수를 '->' 를 통해 뒤이어 이어진 람다식 안의 action 로직에서 사용하는 과정이 되는 것임.
        //     만약, Optional.ofNullable(인수)의 '인수'가 DB에 존재하지 않아 null을 반환하는 경우,
        //     당연히 뒤이어 이어지는 ifPresent 메소드에서는 아무런 동작도 이어지지 않음.
        productService.updateProduct(findBoard, boardPatchDto){

            Optional.ofNullable(boardPatchDto.getContent())
                    .ifPresent(content -> findBoard.setContent(content));

            //순서1) 'Optional.ofNullable(boardPatchDto.getContent())'
            //      : 클라이언트로부터 받아온 Json 객체 데이터 boardPatchDto 의 getContent() 데이터 값이 DB에 있는 경우,
            //        DB로부터 그 boardPatchDto.getContent() 의 값을 조회해서 가져와서 Optional 객체로 그 값을 감싸서 반환함.

            //순서2) '.ifPresent(content -> findProduct.setContent(content))'
            //      : 그 DB로부터 가져온 boardPatchDto.getContent() 필드의 값을 매개변수 인수로 받아들여서
            //        그것을 '사용자 임의로 정한 변수명 content'에 담고,
            //        그 변수명을 뒤이어 이어진 람다식 내부에서 사용하여 DB로부터 조회해 온 수정시키고 싶은 상품 Product 의
            //        새로운 가격으로 넣어주는 것임.

            Optional.ofNullable(boardPatchDto.getTitle())
                    .ifPresent(title -> findBoard.setTitle(title));

            
        //순서3)
        Board updatedBoard = boardRepository.save(findBoard);


        //순서4)
        BoardResponseDto boardResponseDto = boardMapper.productToBoardResponseDto(updatedProduct, updatedBoard);


        return boardResponseDto;

        }

    }




//=============================================================================================================



    //[ 게시글 Board 삭제 Delete ]


    public void deleteBoard(long boardId){


        //순서1) 클라이언트 요청이 삭제하고 싶어하는 게시물이 현재 DB에 존재하는 게시물 Board 인지 확인
        Board findBoard = findVerifiedBoard(boardId);


        //순서2) 클라이언트가 요청이 싶어하는 게시물 안에 있는 상품 Product 가 현재 DB에 존재하는 상품 Product 인지 확인하고,
        //      있다면 조회해서 그 상품 Product 객체를 가져오기
        Product findProduct = productService.findVerifiedProduct(findBoard.getProduct());


        //순서3) 게시글 Board 를 DB에서 삭제
        boardRepository.delete(findBoard);


        //순서4) 상품 Product 를 DB에서 삭제
        productRepository.delete(findProduct);



    }





//=============================================================================================================



    //[ 페이징 ]

    public Page<Board> findBoards(int page, int size) {
        return boardRepository.findAll(PageRequest.of(page, size, Sort.by("boardId").descending()));
    }




    public Page<Board> findBoardsCategory(int category, int page, int size) {
        return  boardRepository.findBoardsByProduct_Category
                (PageRequest.of(page, size, Sort.by("boardId").descending()), category);
    }








//=============================================================================================================







//=============================================================================================================




    //[ DB에 현재 존재하는 게시글 Board 인지 여부를 확인하고, 존재한다면 그 게시글 Board 를 DB로부터 가져와서 반환해주고,
    //  아니라면, Optional로 처리해서 내가 지정한 사용자 정의 에러 ExceptionCode.BOARD_NOT_FOUND 를 발생시켜줌. ]
    public Board findVerifiedBoard(long boardId){

        Optional<Board> optionalBoard = boardRepository.findById(boardId);

        Board findBoard = optionalBoard.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));

        return findBoard;
    }


//=============================================================================================================





























    }

