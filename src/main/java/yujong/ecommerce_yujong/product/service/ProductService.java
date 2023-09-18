package yujong.ecommerce_yujong.product.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yujong.ecommerce_yujong.board.dto.BoardPostDto;
import yujong.ecommerce_yujong.board.entity.Board;
import yujong.ecommerce_yujong.member.entity.Seller;
import yujong.ecommerce_yujong.product.entity.Product;
import yujong.ecommerce_yujong.product.mapper.ProductMapper;
import yujong.ecommerce_yujong.product.repository.ProductRepository;

@RequiredArgsConstructor
@Transactional(readOnly=true)
@Service
public class ProductService {


    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


//================================================================================================================

    //[ DB에 상품 등록 ]
    public Product createProduct(Seller seller, BoardPostDto boardPostDto){
        //순서1) 클라이언트로부터 넘어온 Json 객체 데이터의 본문(body)를 컨트롤러 Board의 개별 각각 컨트롤러 메소드에 붙어 있는
        //      @RequestMapping, @GetMapping, @PostMapping 등을 통해 클라이언트와 연결된 URL에 링크시켜서,
        //      클라이언트로부터 넘어온 JSON 객체 데이터를 먼저 인지해서 캐치 잡아오고,
        //순서2) 컨트롤러 메소드 각각에 상황 필요에 맞게 붙어있는 @RequestBody BoardPostDto boardPostDto 와 같은 것들을 통해
        //      클라이언트로부터 넘어온 Json 객체 데이터를 캐치해서 파싱하고,
        //      이는 BoardPostDto에 자동으로 바인딩됨.

        //      *****증요*****
        //순서3) 이 BoardPostDto 는 오직, 이 클라이언트로부터 넘어온 Json 데이터를 최초에 받아서 캐치하였고
        //      이 메소드 ProductService.createProduct()를 직, 간접적으로 사용하는(연결되어 있는)
        //      (컨트롤러 메소드 postBoard는 그 내부에 메소드 boardService.createBoard() 를 사용하는데,
        //       이 boardService.createBoard() 는 메소드 ProductService.createProduct 를 사용하고 있음)
        //      '컨트롤러 메소드 postBoard((@RequestBody BoardPostDto boardPostDto){..}' 가 그 내부 로직을
        //      실행할 때에만 사용되고 유효한 것임.
        //      '컨트롤러 메소드 postBoard(@RequestBody BoardPostDto boardPostDto){..}'의 내부에 존재하지 않는
        //      메소드, 즉, 컨트롤러 메소드 postBoard가 사용하지 않는 메소드인 예를 들어
        //      이 컨트롤러 외부(당연..)에 위치한 메소드 commentService.deleteComment()에서
        //      BoardPostDto 객체를 당연히 사용할 수 없다!!


        Product product = productMapper.boardPostDtoToProduct(boardPostDto); //'ProductMapper 클래스' 내부 설명 참조.
        //- 이 메소드 createProduct의 인자로 외부에서 들여온 boardPostDto 객체를 '매퍼 ProductMapper'를 사용해서
        //  '상품 Product 엔티티 객체'로 변환시킴.
        //  즉, boardPostDto 객체의 필드 구성에 맞게 커스터마이징(?)한 상품 Product 객체를 생성하는 것임.


        product.setStatus(Product.ProductStatus.PRD_SELLING);
        product.setSeller(seller);

        Product productSaved = productRepository.save(product);
        //*****중요*****
        //< 'JpaRepository의 내장 메소드 save' >
        //: DB에 저장시킨 상품 Product 저장시키고, 그 상품 Product 를 반환해주는 메소드이다!
        //  'save'위에 ctrl 눌러서 확인해보면 'Returns: the saved entities; will never be null.' 일가ㅗ 나옴!

        return productSaved;

    }



//================================================================================================================




















}
