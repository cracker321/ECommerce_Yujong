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
public class MemberController {

    private final MemberService memberService;


//=====================================================================================================================



    //[ 회원 Member 가입 Create ]

    @PostMapping("/members/signup")
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
    @DeleteMapping("/members/{member_id}")
    public ResponseEntity deleteMember(@PathVariable("member_id") Long memberId) {

        memberService.deleteMember(memberId);

        return new ResponseEntity<>("Member removed", HttpStatus.OK);
    }



//=====================================================================================================================



}