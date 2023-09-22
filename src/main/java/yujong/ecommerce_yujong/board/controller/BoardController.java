package yujong.ecommerce_yujong.board.controller;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yujong.ecommerce_yujong.board.dto.BoardPatchDto;
import yujong.ecommerce_yujong.board.dto.BoardPostDto;
import yujong.ecommerce_yujong.board.dto.BoardResponseDto;
import yujong.ecommerce_yujong.board.entity.Board;
import yujong.ecommerce_yujong.board.mapper.BoardMapper;
import yujong.ecommerce_yujong.board.service.BoardService;

@Data
@AllArgsConstructor
//[ 스프링이 아닌 Java 자체만을 사용하는 경우 ]. 우연히 어찌되었든 스프링 JPA에서 Entity 객체를 사용하는 경우에도 아래 내용이 적용됨.
//(근데 사실, 스프링이 아닌 Java 자체만을 사용하는 경우인데, @AllArgsConstructor와 같은 어노테이션을 붙인다는 게 말이 안 되긴 하지만,
// 어거지로 어찌됐든 설명하자면 아래와 같다는 말임!)
//- 사용자 정의 생성자가 없는 경우에는 '기본 생성자'가 기본적으로 숨겨져 있지만,
//  이렇게 @AllArgsConstructor를 사용하여 '모든 매개변수를 포함하는 생성자'를 만드는 경우나
//  @ReuqiredArgsConstructor를 사용하여 final 또는 @NonNull이 붙은 필드를 선별하여(=Requirerd)
//  그 필요로 하게 되는 필드만(=Required)을 매개변수로 하는 생성자를 만들어주는 경우,
//  모든 매개변수를 포함하는 생성자나 final 또는 @NonNull이 붙은 필드를 매개변수로 갖는 생성자나 어찌되었든 '사용자 정의 생성자'이고,
//  그에 따라 아무 매개변수도 들어가 있지 않은 '기본 생성자'를 직접 'public BoardController(){}' 이렇게 생성하든가,
//  아니면, @NoArgsConstructor를 어노테이션 붙여줘야 한다!


//*****매우매우 중요*****
//[ 스프링을 사용하는 경우 ]

//< 1. 스프링 컨테이너가 관리해주는 Bean 객체인 경우 >
//- 스프링 컨테이너가 관리해주는 Bean 객체(Controller, Service, Repository, Compenent, Configuration, Interceptor,
//  Filter, Aspect, TaskExecutor, Listner 클래스 등)의 클래스에서는,
//  그 클래스 위에 @AllArgsConstructor를 붙여서 그 클래스 내부의 모든 필드를 매개변수로 갖는 생성자를 만들어주거나,
//  @RequqiredArgsConstructor를 붙여서 그 클래스 내부에 있는 fianl 또는 @NonNull이 붙은 필드를 매개변수를 갖는 생성자와 같은
//  '사용자 정의 생성자'를 만들어줘서 '기본 생성자'를 작성해야 하는 상황을 발생시켜도,
//  이 Bean 객체들은 스프링 컨테이너가 자동으로 관리해주어 '기본 생성자'를 스프링 컨테이너가 대신 자동으로 생성해주기 때문에,
//  개발자가 이 경우에 '별도의 기본 생성자를 따로 작성해주거나 @NoArgsConstructor를 붙이지 않아도' 잘 작동한다!


//< 2. JPA에서의 Entity 클래스인 경우 >
//- 그러나, 'JPA를 사용했을 때의 Entity 클래스'의 경우는 @AllArgsConstuctor 또는 @RequiredArgsConstructor를 Entity 클래스 위에
//  붙일 경우, 반드시
//  1) 'public 또는 protected를 사용한 기본 생성자 직접 작성' 또는
//  2) 엔티티 클래스 위에 '@NoArgsConstructor 붙이기'가 필요하다!!
//  위 중에서 1)을 선택하고 싶은 경우, 엔티티 클래스 내부에 개발자가 직접 기본 생성자를 작성할 때는 private을 붙이면 안된다!!
//- 왜냐하면, JPA에서 엔티티 클래스를 사용하는 경우에는, JPA 구현체(Hibernate 등)가 엔티티 객체를 생성할 때,
//  '리플렉션'을 사용하기 때문에, 반드시 그 엔티티 클래스 내부에서 '매개변수가 없는 기본 생성자'를 작성해줘야 한다!
//  그리고, 그 기본 생성자의 접근제어자는 반드시 public 또는 protected로 선언되어야 하고,
//  private으로 선언되면 안된다!
//- JPA에서의 Java 리플렉션은, JPA 구현체(Hibernate 등)의 실행 시점에 클래스의 메타데이터 정보를 얻거나, 수정하거나, 메소드와 필드에
//  접근할 수 있도록 해주는 Java API임.
//  JPA가 엔티티 객체를 생성할 때도 이 Java 리플렉션을 사용하는데, 이 때 인자(매개변수)가 없는 기본 생성자가 필요함.
//  왜냐하면, Java 리플렉션으로 객체를 생성할 때 Calss.newInstance() 메소드 또는 Constructor,newInstance() 메소드 등을 사용하는데,
//  이들 메소드는 모두 인자(매개변수)가 없는 기본 생성자를 호출하기 때문임.
//  만약 해당 클래스에 기본 생성자가 없거나, 있더라도 그 기본 생성자가 private으로 선언되어 있는 상태라면,
//  위 메소드들은 IllegalAccessException을 발생시켜 객체 생성에 실패하게 됨.
//  따라서, JPA를 사용할 때는 엔티티 클래스 내부에 반드시 public 또는 protected로 선언된 (인자(매개변수)가 없는)기본 생성자가
//  반드시 있어야 한다!
//  그렇기에, 이 때는 기본 생성자를 직접 작성해주거나(e.g: public Board(){} 또는 protected Board(){}),
//  @NoArgsConstructor를 그 엔티티 클래스 위에 붙이면 된다!!
//  - JPA에서 Entity 클래스 작성 시 반드시 지켜야 하는 규칙
//    (1) 기본 생성자 필요
//    (2) @Entity 어노테이션
//    (3) ID 어노테이션: 엔티티의 기본키(primary key)를 나타냄. 각 엔티티 객체를 유일하게 식별하기 위해 필요함.


//- @RequiredArgsConstructor는 final과 @NonNull만 붙은 필드를 선별하여(=Requirerd) 그 필요로 하게 되는 필드만(=Required)을
//  매개변수로 하는 생성자를 만들어주는 것이므로,
//  절대 @RequiredArgsConstructor가 '기본 생성자를 작성해주거나 @NoArgsConstructor를 포함하고 있지 않다'!!!


//*****중요*****
//cf) - 컨트롤러 클래스에는 일반적으로 @AllArgsConstructor 나 @NoArgsConstructor를 사용하지 않음.
//      왜냐하면, 컨트롤러 클래스는 웹 요청을 처리하는 역할만 수행하해야 하고, 따라서 컨트롤러가 여러가지 다른 Serivce, Repository를
//      컨트롤러 클래스 내부에 private final MemberService memberSerive와 같이 의존성 주입받는 경우는 1개 또는 소수의 서비스만을
//      필요로 하는 것이 대부분이다.
//    - 즉, 예를 들어, MemberController는 MemberService만 private final MemberService memberService를 통해
//      의존성주입 받는 것이 일반적이고,
//      만약에 MemberController가 MemberService, OrderService, ReviewService, MailService 등 4개의 Service를
//      동시에 의존하고 있다면, 이 MemberController는 회원, 주문, 리뷰, 메일 기능까지 처리하고 있다는 것을 의미하는 것이고,
//      이는 결코 바람직하지 않으며, 이럴 경우 각각의 위 4개의 기능에 대해 각각의 별도의 개별 컨트롤러를 만드는 것이 일반적이다!
//    - 따라서, @AllArgsConstructor를 사용하여 굳이 컨트롤러 내부의 모~든 필드 값을 매개변수로 받는 생성자로 만들어 줄 필요 없이,
//      RequiredArgsConstructor를 사용하여 final이나 @NonNull이 붙은 필드만을 사용하는 생성자를 만들어주는 것이 더 바람직하다!
//      왜냐하면, @AllArgsConstructor를 사용하여 컨트롤러 내부의 모~든 필드를 매개변수로 받는 생성자를 자동으로 만들어버리면,
//      '실제로 필요하지 않는 의존성까지 주입받게 될 수 있기 때문'에, 이렇게 하는 것보다
//      final 또는 @NonNull이 붙어있는 필드만을 선택적으로 골라서 그 필드만을 매개변수로 갖는 생성자를 만들어주는 것이 더 현명하다!
//      뭐 굳이 따지자면, 어쨌든 @AllArgsConstructor를 사용하면 모든 필드를 갖는 생성자를 만들어주기 때문에,
//      당연히 final과 @NonNull이 붙은 필드를 매개변수로 포함하는 생성자를 만들어주고,
//      이에 따라, 당연히 final과 @NonNull이 붙은 필드를 생성자 주입 받게 되는 것도 맞음. 그런데 이러지 말자는 거지!
//      왜냐하면, 스프링에서는 일반적으로 필요한 의존성만 주입받도록 설계하는 것이 바람직하다고 권장하기 때문에!!


//@NoArgsConstructor: 매개변수가 없는 기본 생성자를 만들어줌.
//@RequiredArgsConstructor: //@RequiredArgsConsturctor 어노테이션을 사용하면,
                            //final로 선언된 필드나 @NonNull 어노테이션을 사용한 필드만을 필요로 하는 생성자를 만들어준다!

//cf) '회원 엔티티 Member', '배송 엔티티 Delivery'와 같은 엔티티 객체들은 컨트롤러, 서비스, 레펏 등 여기저기에 다 걸쳐가며
//    왔다갔다하며 사용되는 것이고, 따라서 이 엔티티 객체들은 의존성 주입의 대상이 아님!
//    의존성 주입의 대상은 대부분 Service, Repository 이다!!
@RequestMapping("/boards")
@RequiredArgsConstructor
@RestController //@Controller + @ResponseBody
public class BoardController {


    private final BoardService boardService;
    private final BoardMapper boardMapper;




//=============================================================================================================


    //[ 게시글 Board 등록 Create ]
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


//=============================================================================================================


    //[ 게시글 Board 수정 Update ]


    @PatchMapping("/{board_id}")
    public ResponseEntity patchBoard(@PathVariable("board_id") long boardId,
                                     @RequestBody BoardPatchDto boardPatchDto){

        BoardResponseDto response = boardService.updateBoard(boardId, boardPatchDto);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }



//=============================================================================================================



    //[ 게시글 Board 삭제 Delete ]

    @DeleteMapping("/{board_id}")
    public ResponseEntity deleteBoard(@PathVariable("board_id") long boardId){

        boardService.deleteBoard(boardId);

        return new ResponseEntity<>("Removal Success", HttpStatus.OK);
    }




//=============================================================================================================


    //[ 단일 게시글 Board 조회 Read ]

    @GetMapping("/{board_id}")
    public ResponseEntity getBoard(@PathVariable("board_id") long boardId,
                                   BoardResponseDto baordResponseDto){

        Board findBoard = boardService.findVerifiedBoard(boardId);

        BoardResponseDto boardResponseDto = boardMapper.boardToBoardResponseDto(findBoard);

        return new ResponseEntity<>(boardResponseDto, HttpStatus.OK);

    }


//=============================================================================================================


    //[ 전체 게시글 Board 조회 Read ]








//=============================================================================================================

}
