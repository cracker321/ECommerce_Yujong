package yujong.ecommerce_yujong.global.response;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class SingleResponseDto<T> {

    /*


    [ SingleResponseDto ]

    - 1개의 데이터타입만을 반환할 때 사용함.
      즉, 단일 객체만을 포장할 수 있음.
      제네릭타입 <T> 를 사용하여, 그 1개의 데이터타입을 여기서 포함시킬 때, 어떤 타입의 객체라도 그 1개의 타입으로
      선택되기 위한 후보군이 될 수 있음.
      예를 들어, 사용자정보 Member 엔티티 또는 상품정보 Product 엔티티 등에 대한 정보를
      서버에서 클라이언트로 보낼 때, 둘 중 하나로 선택되기만 한다면,
      어떠한 타입의 엔티티라도 클라이언트에게 보낼 수 있음.

    *****중요*****
    - 외부 클래스에서 SingleResponseDto 객체를 아래와 같이 사용할 수 있음.

      < 1. Member 엔티티 클래스 >

      @Data
      @Entity
      public class Member{

            @Id
            @GeneratedValue(..)
            private Long memberId;

            @Column
            private String name;

            @Column
            private String email;

      }




      < 2. SingleResponseDto 클래스 >

      @Data
      @AllArgsConstructor
      public class SingleResponseDto<T>{

            private T data;    <<--- 이 'T'에 'Member'가 들어가게 되어, '필드 data'는 이제 '회원 Member' 객체를 담고 있는 거이 된다!

      }





      < 3. SingleResponseDto 클래스를 객체로 활용해서 사용한 MemberController >

      @RestController
      @RequestMapping("/members")
      public class MemberController{

            @Autowired
            private MemberService memberService;


            //< 회원 Member 조회 Read >
            @GetMapping("/{id}")
            public ResponseEntity<SingleResponoseDto<Member>> getMember(@PathVariable Long memberId){

                    Member member = memberService.getMember(memberId);

                    SingleResponseDto<Member> response = new SingleResponseDto<>(member);

                    return new RespoonseEntity(response, HttpStatus.OK);

            }




      < 4. 이 때 클라이언트가 전달해준 memberId에 해당하는 DB에 저장되어 있는 회원 Member 객체가 아래와 같다면, >

      - 아래는 DB 내부
      
      id: 1
      name: "Yujong Cho"
      email: "yujong@gmail.com"






      < 5. 서버가 클라이언트에게 응답해주는 Http 응답 본문은 아래와 같은 JSON 객체 데이터로 표현됨 >

      {
        "data" {           <--- 즉, SingleResponseDto 내부의 필드 data 는, 이제 '회원 Member 객체 그 자체'를 담고 있는 것이 된다!
            "id": 1,
            "name": "Yujong Cho"
            "email": "yujong@gmail.com"
            }
      }



      e.g)
    - 비유&실생활
      : 단일 상품을 포장하여 팔기 위한 상자.
     */

    private T data;


}