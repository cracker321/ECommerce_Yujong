package yujong.ecommerce_yujong.product.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yujong.ecommerce_yujong.board.dto.BoardPatchDto;
import yujong.ecommerce_yujong.board.dto.BoardPostDto;
import yujong.ecommerce_yujong.board.entity.Board;
import yujong.ecommerce_yujong.global.exception.BusinessLogicException;
import yujong.ecommerce_yujong.member.entity.Seller;
import yujong.ecommerce_yujong.product.entity.Product;
import yujong.ecommerce_yujong.product.mapper.ProductMapper;
import yujong.ecommerce_yujong.product.repository.ProductRepository;

import java.util.Optional;

import static yujong.ecommerce_yujong.global.exception.ExceptionCode.PRODUCT_NOT_FOUND;

@RequiredArgsConstructor
@Transactional(readOnly=true)
@Service
public class ProductService {


    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


//================================================================================================================

    //[ DB에 상품 Product 등록 Create ]

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




    //[ DB에 상품 Product 수정 Update ]
    //- 게시글 Board 를 수정할 때(BoardPatchDto), 상품 Product 도 수정하는 경우 있으니,
    //  그 때 사용할 목적으로 메소드 updateProduct 를 작성함.
    public Product updateProduct(Board findBoard, BoardPatchDto boardPatchDto){

        //순서1) < 'ProductService.findVerifiedProduct()':
        //        DB에 현재 존재하는 상품 Product 인지 여부를 확인하고, 존재한다면 그 상품 Product 를 가져와서 반환해주고,
        //        아니라면, Optional로 처리해서 내가 지정한 사용자 정의 에러 ExceptionCode.PRODUCT_NOT_FOUND 를 발생시켜줌. >
        Product findProduct = findVerifiedProduct(findBoard.getProduct());


        //순서2) 상품 Product 업데이트

        //[ Optional.ofNullalbe(인수).ifPresent(인수<> action) ]
        //
        //- 'Optional.ofNullable(인수)'
        //   : DB를 조회해본 후에, 주어진 인수에 해당하는 데이터가 DB에 없는 경우(null)라면, Optional.empty() 객체를 반환하고,
        //     이 때 반환된 Optional.empty() 객체를 예외 처리 해주기 위해,
        //     이 반환된 객체를 참조하는 변수에 대해 그 아래 줄에 반드시 orElseThrow를 작성해줘야 한다!
        //     만약 DB를 조회해본 휑, 주어진 인수에 해당하는 데이터가 DB에 있는 경우라면,
        //     당연히 이 때 주어진 인수를 감싸고 있는 Optional 객체를 반환해줌.
        //     그리고 이 때도, 반환된 Optional 객체에서 Optional 을 벗겨내기 위해 orElseThrow 를 위에서와 같이 작성해줘야 함.
        //
        // - 'ifPresent(사용자 정의 변수명(아무거나 해도 되. a, b, c 등등) -> action)'
        //   : Optional.ofNullable(인수)에 이어져서 호출되는 Optional 클래스의 내장 메소드이며,
        //     Optional.ofNullable(인수)의 '인수'가 DB에 존재하는 경우 그 인수를 조회해서 가져오고,
        //     그 db로부터 가져온 인수를 감싸고 있는 Optional 객체가 반환되는데,
        //     그 db로부터 가져온 인수를 감싸고 있는 Optional 객체를 인수로 받아들이고, 여기서 '사용자 정의 변수명'이
        //     그 db로부터 가져온 인수를 감싸고 있는 Optional 객체를 참조하고,
        //     그 변수를 '->' 를 통해 뒤이어 이어진 람다식 안의 action 로직에서 사용하는 과정이 되는 것임.


        //     값으로 null 이 아닌 것을 가지고 있을 때만 그 인수를 감싸고 있는 Optional 객체를 '
        //     뒤이어서 action 도 실행해줌.
        //     Optional 객체가 비어 있는 null 값인 경우, 아무 동작도 하지 않음.

        Optional.ofNullable(boardPatchDto.getPrice())
                .ifPresent(price -> findProduct.setPrice(price));
        //순서1) 클라이언트로부터 받아온 Json 객체 데이터 boardPatchDto 의 getPrice() 데이터가 DB에 있는 경우,
        //      그 boardPatchDto



    }




//================================================================================================================



    //[ DB에 현재 존재하는 상품 Product 인지 여부를 확인하고, 존재한다면 그 상품 Product 를 가져와서 반환해주고,
    //  아니라면, Optional 로 처리해서 내가 지정한 사용자 정의 에러 ExceptionCode.PRODUCT_NOT_FOUND 를 발생시켜줌. ]

    public Product findVerifiedProduct(Product product){

        Optional<Product> optionalProduct = productRepository.findById(product.getProductId());

        Product findProduct = optionalProduct.orElseThrow(() -> new BusinessLogicException(PRODUCT_NOT_FOUND));

        return findProduct;
    }





//================================================================================================================



//================================================================================================================




//================================================================================================================




//================================================================================================================




}
