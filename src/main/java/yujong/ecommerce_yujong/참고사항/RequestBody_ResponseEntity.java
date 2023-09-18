package yujong.ecommerce_yujong.참고사항;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import yujong.ecommerce_yujong.board.dto.BoardPostDto;
import yujong.ecommerce_yujong.board.dto.BoardResponseDto;
import yujong.ecommerce_yujong.member.entity.Seller;
import yujong.ecommerce_yujong.product.entity.Product;

public class RequestBody_ResponseEntity {




    //< 게시글 등록 >
    @PostMapping()
    public ResponseEntity postBoard(@RequestBody BoardPostDto boardPostDto){

        BoardResponseDto response = boardService.createBoard(boardPostDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }


    /*
    [ @RequestBody & ReponseEntity ]

    순서1) 클라이언트가 새 게시글을 작성하고 싶어서 여기 백엔드의 컨트롤러로 아래 예시와 같은 Http RequestBody(요청 본문)을
           JSON 객체 데이터로 보내옴.

    {
    "title": "New Post",
    "content": "This is a new post."
    }


    순서2) '@RequestBody BoardPostDto boardPostDto'
          : 클라이언트로부터 받은 HTTP 요청 본문(JSON 객체 데이터 형식)을 아래 예시와 같은 BoardPostDto 타입의
            자바 객체 BoardPostDto로 변환하는 역할을 함.

    BoardPostDto {
    title = "New Post",
    content = "This is a new post."
    }


    순서3) 변환된 자바 객체(baordPostDto)가 컨트롤러 메소드 postBoard의 매개변수(인자. BoardPostDto boardPostDto)로 전달됨.
          즉, 이제 클라이언트로부터 넘어온 JSON 객체 데이터를 자바 객체 BoardPostDto로 변환시켰고,
          그것을 이 컨트롤러 메소드 postBoard의 매개변수(인자. BoardPostDto boardPostDto)로 받아들였기 때문에,
          그 넘어온 JSON 객체 데이터를 이 컨트롤러 메소드 postBoard 안에서 사용할 수 있게 되는 것임.


    순서4) 'boardService.createBoard(boardPostDto)'
          : 실제로 게시글을 생성하는 로직.
            boardPostDto에 담긴 정보(제목 title, 내용 content 등)을 사용하여 새 게시글 Board를 만들어서 DB에 저장하고,
            그 결과(새롭게 만든 새 게시글 Board의 정보, 즉 BoardResponseDto 객체)를 반환해옴.
            즉, boardService의 createBoard() 메소드는, 전달받은 BoardPostDto 객체를 DB에 저장하고,
            그 결과(새롭게 만든 새 게시글 Board의 정보. BoardResponseDto 객체)를 반환해줌.
            반환받은 BoardResponseDto 객체의 내부 정보 예시는 아래와 같음

    BoardResponseDto {
    "id": 1,
    "title": "New Post",
    "content": "This is a new post.",
    "createdAt": "2023-09-14T12:00:00"
    }


    순서5) 반환받은 BoardResponseDto 객체를 변수 response에 담음.


    순서6) 'return new ResponseEntity<>();'
          : 'ResponseEntity 객체'를 생성함.
            'ResponseEntity 객체'는 '스프링 웹 MVC 모듈에 포함되어 있는 내장 클래스'를 사용하여 생성할 수 있는 '내장 객체'임.
            즉, 처음에 스프링 프로젝트를 만들 때, 스프링 웹 모듈을 포함하면, 'ResponseEntity 클래스'는 자동으로 '내장 클래스'로
            되어서, 따로 내가 ResponseEntity 클래스를 별도로 만들지 않아도 바로 'ResponseEntity 객체'를 new 연산자 사용해서
            만들 수 있는 것임!
            사실, 위 ResponseEntity 객체 생성을 코드로 표현하면 아래와 같음.
            ResponseEntity<BoardResponseDto> responseEntity = new ResponseEntity<>(BoardResponseDto);


    순서7) 'return new ResponseEntity<>(response, HttpStatus.CREATED)'
          : 생성한 ResponseEntity 객체의
            첫 번째 매개변수(인자) 'response(즉, BoardResponseDto)'는 'Http 응답 본문(Body)'이고,
            두 번째 매개변수(인자) 'HttpStatus.CREATED)'는 'Http 응답 상태 코드(Status Code)로 설정됨.


    순서8) 최종적으로 클라이언트에 넘겨주는 Http 응답은 아래 예시와 같은 형태를 가지며, 아 Http 응답의 본문(body)으로는
          당연히 BoardResponseDto 객체가 그 응답 본문(body)으로 들어감.

    HTTP/1.1 201 Created    // Http 상태 코드
    Content-Type: application/json    // Header 중 'Content-Type': Http 응답 본문이 JSON 형식임을 말해줌.
    Date: Wed, 14 Sep 2023 12:10:00 GMT    // Header 중 'Date': Http 응답이 생성된 시간을 말해줌.

    {   //아래는 Http 응답의 '응답 본문 ResponseBody'
        //클라이언트에게 넘겨주는 'JSON 형식의 실제 데이터'를 의미하며, 이 데이터는 클라이언트에게 전송됨.
    "id": 1,
    "title": "New Post",
    "content": "This is a new post.",
    "createdAt": "2023-09-14T12:00:00"
    }



     */


    //================================================================================================================

    /*

    [ 상품 등록 ]


    < 컨트롤러 메소드 createProduct >

    public Product createProduct(Seller seller, BoardPostDto boardPostDto){


        product.setStatus(Product.ProductStatus.PRD_SELLING);
        product.setSeller(seller);

        Product productSaved = productRepository.save(product);
        *****중요*****
        < 'JpaRepository의 내장 메소드 save' >
        : DB에 저장시킨 상품 Product 저장시키고, 그 상품 Product 를 반환해주는 메소드이다!
          'save'위에 ctrl 눌러서 확인해보면 'Returns: the saved entities; will never be null.' 이렇게 나옴!


        return productSaved;

    }

        *****중요*****

        순서1) 클라이언트로부터 넘어온 Json 객체 데이터의 본문(body)를 컨트롤러 Board의 개별 각각 컨트롤러 메소드에 붙어 있는
              @RequestMapping, @GetMapping, @PostMapping 등을 통해 클라이언트와 연결된 URL에 링크시켜서,
              클라이언트로부터 넘어온 JSON 객체 데이터를 먼저 인지해서 캐치 잡아오고,
        순서2) 컨트롤러 메소드 각각에 상황 필요에 맞게 붙어있는 @RequestBody BoardPostDto boardPostDto 와 같은 것들을 통해
              클라이언트로부터 넘어온 Json 객체 데이터를 캐치해서 파싱하고,
              이는 BoardPostDto에 자동으로 바인딩됨.

              *****중요*****
        순서3) 이 BoardPostDto 는 오직, 이 클라이언트로부터 넘어온 Json 데이터를 최초에 받아서 캐치하였고
              이 메소드 ProductService.createProduct()를 직, 간접적으로 사용하는(연결되어 있는)
              (컨트롤러 메소드 postBoard는 그 내부에 메소드 boardService.createBoard() 를 사용하는데,
               이 boardService.createBoard() 는 메소드 ProductService.createProduct 를 사용하고 있음)
              '컨트롤러 메소드 postBoard((@RequestBody BoardPostDto boardPostDto){..}' 가 그 내부 로직을
              실행할 때에만 사용되고 유효한 것임.
              '컨트롤러 메소드 postBoard(@RequestBody BoardPostDto boardPostDto){..}'의 내부에 존재하지 않는
              메소드, 즉, 컨트롤러 메소드 postBoard가 사용하지 않는 메소드인 예를 들어
              이 컨트롤러 외부(당연..)에 위치한 메소드 commentService.deleteComment()에서
              BoardPostDto 객체를 당연히 사용할 수 없다!!





     */
}
