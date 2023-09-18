package yujong.ecommerce_yujong.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yujong.ecommerce_yujong.board.dto.BoardPostDto;
import yujong.ecommerce_yujong.board.entity.Board;
import yujong.ecommerce_yujong.board.mapper.BoardMapper;
import yujong.ecommerce_yujong.member.entity.Seller;
import yujong.ecommerce_yujong.member.service.SellerService;
import yujong.ecommerce_yujong.product.entity.Product;
import yujong.ecommerce_yujong.product.repository.ProductRepository;
import yujong.ecommerce_yujong.product.service.ProductService;

@RequiredArgsConstructor
@Transactional(readOnly=true)
@Service
public class BoardService {

    private final SellerService sellerService;
    private final BoardMapper boardMapper;
    private final ProductService productService;
    private final ProductRepository productRepository;

    public Board createBoard(Long sellerId, BoardPostDto boardPostDto){

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


        //< DB에 현재 존재하는 판매자 Seller 인지 여부를 확인하고, 존재한다면 그 판매자 Seller를 가져와서 반환해주고,
        //  아니라면, Optional로 처리해서 내가 지정한 사용자 정의 에러 ExceptionCode.MEMBER_NOT_FOUND 를 발생시켜줌. >
        Seller findSeller = sellerService.findVerifiedSeller(sellerId);


        //< 게시글 Board 내에 들어갈 DB에 이미 저장시킨 상품 Product에 필요한 상품 Product 그 자체의(자체에) 필요한 정보 입력 >
        //- 'productService.createProduct'는 'DB에 등록 저장시키는 BoardPostDto 객체의 필드 구성에 맞게
        //   커스터마이징된 DB에 저장 등록시킬 상품 Product 엔티티 객체를 DB에 등록 저장시키고 그 저장시킨 Product를 꺼내와서 반환함.
        Product product = productService.createProduct(findSeller, boardPostDto);


        //< 위에서의 '판매자 Seller'와 '게시글 Board 내에 들어갈 상품 Product 자체 정보'를 바탕으로 이제 '게시글 Board'를 DB에 등록 >
        Board board = boardMapper.boardPostDtoToBoard(boardPostDto);


    }


        
        































    }

}
