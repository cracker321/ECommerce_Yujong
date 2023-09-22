package yujong.ecommerce_yujong.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yujong.ecommerce_yujong.member.dto.LoginRequestDto;
import yujong.ecommerce_yujong.member.dto.LoginResponseDto;
import yujong.ecommerce_yujong.member.entity.Member;
import yujong.ecommerce_yujong.member.role.Role;
import yujong.ecommerce_yujong.member.service.MemberService;

/* 공통의 회원 클래스 컨트롤러 : 회원가입, 로그인, 회원 탈퇴 등을 구현 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;


//=====================================================================================================================



    //[ 회원 Member 가입 Create ]

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody LoginRequestDto loginRequestDto,
                                 LoginResponseDto loginResponseDto,
                                 Member member) {


        member.setEmail(loginRequestDto.getEmail());
        member.setPassword(loginRequestDto.getPassword());

        if (loginRequestDto.getRole() == Role.CUSTOMER) {
            // 고객 Custoemr 회원가입 로직
            member.setRole(Role.CUSTOMER);
        } else if (loginRequestDto.getRole() == Role.SELLER) {
            // 판매자 Seller 회원가입 로직
            member.setRole(Role.SELLER);
        } else {
            return ResponseEntity.badRequest().body(null);  // 잘못된 Role인 경우
        }

        // 회원가입 로직 실행
        Member createdMember = memberService.createMember(member);

        loginResponseDto.setMemberId(createdMember.getMemberId());
        loginResponseDto.setEmail(createdMember.getEmail());
        loginResponseDto.setRole(createdMember.getRole());
        loginResponseDto.setName(createdMember.getName());

        return new ResponseEntity<>(loginResponseDto, HttpStatus.CREATED);
    }




//=====================================================================================================================




    //[ 회원 Member 삭제 Delete ]
    @DeleteMapping("/{memberId}")
    public ResponseEntity deleteMember(@PathVariable Long memberId) {

        memberService.deleteMember(memberId);

        return new ResponseEntity<>("Member removed", HttpStatus.OK);
    }


//=====================================================================================================================



    //스프링시큐리티에 의해 로그인은 자동 처리되므로 작성 안 해도 됨.
    

//    //[ 회원 Member 로그인 login ]
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
//
//        LoginResponseDto loginResponseDto = memberService.login(loginRequestDto);
//
//        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
//    }




//=====================================================================================================================



    //스프링시큐리티에 의해 로그아웃은 자동 처리되므로 작성 안 해도 됨.

//    //[ 회원 Member 로그아웃 logout ]
//    @GetMapping("/logout")
//    public ResponseEntity<Void> logout(HttpSession session) {
//
//
//        memberService.logout(session);
//
//        return new ResponseEntity<>(HttpStatus.OK);
//        //위 return new ResponseEntity<>(HttpStatus.OK); 와 동일한 다른 표현 방법.
//        //- return ResponseEntity.ok().build();
//        //  * 'ok()': HTTP 200 OK 상태코드이며, 성공적인 응답을 나타냄
//        //  * 'build()': 현재 설정된 Http 상태코드('200 OK')와 헤더를 사용하여, 최종적으로 ResponseEntity 객체를 완성함.
//        //               주로, '본문 body 없는 응답 ReponseEntity 객체'를 생성할 때 주로 사용됨.
//
//        /*
//        (1) Http 200 OK 상태와 함께 본문 body 없는 응답
//            1) return new ResponseEntity<>(HttpStatus.OK)
//            2) reutrn ResponseEntity.ok().build()
//
//        (2) Http 200 OK 상태와 함께 특정 본문 body 있는 응답
//            return new ResponseEntity<>(응답본문 body, HttpStatus.OK);
//
//        (3) 에러 등 다른 상태코드와 함께 특정 본문 body 있는 응답
//            return new ResponseEntity<>(에러미시지, HttpStatus.BAD_REQUEST);
//
//         */
//
//    }



//=====================================================================================================================




//=====================================================================================================================

}