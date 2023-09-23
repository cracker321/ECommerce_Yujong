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
public class BoardService {

    private final SellerService sellerService;
    private final BoardMapper boardMapper;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final BoardRepository boardRepository;





    /* 게시글 Board 등록 Create */

    public BoardResponseDto createBoard(BoardPostDto boardPostDto) {


        Seller findSeller = sellerService.findVerifiedSeller(boardPostDto.getSellerId());


        Product product = productService.createProduct(findSeller, boardPostDto);


        Board board = boardMapper.boardPostDtoToBoard(boardPostDto);
        board.setSeller(findSeller);
        board.setProduct(product);
        product.setLeftStock(product.getStock());
        boardRepository.save(board); //DB에 게시글 Board 최종 등록 저장


        BoardResponseDto boardResponseDto = boardMapper.productToBoardResponseDto(product, board);

        return boardResponseDto;


    }




    /* 게시글 Board 수정 Update */

    public BoardResponseDto updateBoard(BoardPatchDto boardPatchDto, long boardId) {


        Board findBoard = findVerifiedBoard(boardPatchDto.getBoardId());

        Product updatedProduct = productService.updateProduct(findBoard, boardPatchDto);

        Optional.ofNullable(boardPatchDto.getContent())
                    .ifPresent(content -> findBoard.setContent(content));

        Optional.ofNullable(boardPatchDto.getTitle())
                    .ifPresent(title -> findBoard.setTitle(title));

        Board updatedBoard = boardRepository.save(findBoard);

        BoardResponseDto boardResponseDto = boardMapper.productToBoardResponseDto(updatedProduct, updatedBoard);

        return boardResponseDto;

        }





//=============================================================================================================


    /* 단일 게시글 Board 조회 Read */

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



    /* DB에 존재하는 게시글 Board 인지 여부 확인 */
    public Board findVerifiedBoard(long boardId){

        Optional<Board> optionalBoard = boardRepository.findById(boardId);

        Board findBoard = optionalBoard.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));

        return findBoard;
    }





    /* 전체 게시글 Board 조회 Read */
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




    /* 게시글 Board 삭제 Delete */


    public void deleteBoard(long boardId){


        Board findBoard = findVerifiedBoard(boardId);

        Product findProduct = productService.findVerifiedProduct(findBoard.getProduct());

        boardRepository.delete(findBoard);

        productRepository.delete(findProduct);

    }




    /* 전체 게시글 Board 페이징 Paging */
    public Page<Board> findBoards(int page, int size) {
        return boardRepository.findAll(PageRequest.of(page, size, Sort.by("boardId").descending()));
    }



}

