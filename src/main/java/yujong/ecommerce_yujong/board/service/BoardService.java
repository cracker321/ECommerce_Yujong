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
import yujong.ecommerce_yujong.board.dto.BoardTotalResponseDto;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    public BoardResponseDto updateBoard(BoardPatchDto boardPatchDto, long boardId) {


        //순서1)
        Board findBoard = findVerifiedBoard(boardPatchDto.getBoardId());


        //순서2) 상품 Product 업데이트
        Product updatedProduct = productService.updateProduct(findBoard, boardPatchDto);



        Optional.ofNullable(boardPatchDto.getContent())
                    .ifPresent(content -> findBoard.setContent(content));

        Optional.ofNullable(boardPatchDto.getTitle())
                    .ifPresent(title -> findBoard.setTitle(title));

            
        //순서3)
        Board updatedBoard = boardRepository.save(findBoard);


        //순서4)
        BoardResponseDto boardResponseDto = boardMapper.productToBoardResponseDto(updatedProduct, updatedBoard);


        return boardResponseDto;

        }





//=============================================================================================================


    //[ 단일 게시글 Board 조회 Read ]

    public BoardResponseDto getBoard(long boardId) {

        //게시판 존재 여부 화인
        Board findBoard = findVerifiedBoard(boardId);

        //판매자 존재 여부 확인
        Seller findSeller = sellerService.findVerifiedSeller(findBoard.getSeller().getSellerId());

        //상품 존재 여부 확인
        Product findProduct = productService.findVerifiedProduct(findBoard.getProduct());

        BoardResponseDto responseDto = boardMapper.productToBoardResponseDto(findProduct , findBoard);

        return  responseDto;
    }






    //[ DB에 현재 존재하는 게시글 Board 인지 여부를 확인하고, 존재한다면 그 게시글 Board 를 DB로부터 가져와서 반환해주고,
    //  아니라면, Optional로 처리해서 내가 지정한 사용자 정의 에러 ExceptionCode.BOARD_NOT_FOUND 를 발생시켜줌. ]


    public Board findVerifiedBoard(long boardId){

        Optional<Board> optionalBoard = boardRepository.findById(boardId);

        Board findBoard = optionalBoard.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));

        return findBoard;
    }





//=============================================================================================================



    //[ 전체 게시글 Board 조회 Read ]


    public List<BoardTotalResponseDto> getBoards(List<Board> boardList) {
        List<BoardTotalResponseDto> totalBoard = new ArrayList<>();

        Iterator iter = boardList.iterator();

        while (iter.hasNext()) {
            Board board = (Board) iter.next();
            Product product = board.getProduct();
            totalBoard.add(boardMapper.productToBoardTotalResponseDto(product, board));
        }
        return totalBoard;
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

    //[ 전체 게시글 Board 페이징 Paging ]
    public Page<Board> findBoards(int page, int size) {
        return boardRepository.findAll(PageRequest.of(page, size, Sort.by("boardId").descending()));
    }



    //[ 카테고리별 게시글 Board 페이징 Paging ]
    public Page<Board> findBoardsCategory(int category, int page, int size) {
        return  boardRepository.findBoardsByProduct_Category
                (PageRequest.of(page, size, Sort.by("boardId").descending()), category);
    }


//=============================================================================================================



}

