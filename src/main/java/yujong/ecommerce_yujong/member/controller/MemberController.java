package yujong.ecommerce_yujong.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yujong.ecommerce_yujong.member.dto.MemberDto;
import yujong.ecommerce_yujong.member.entity.Member;
import yujong.ecommerce_yujong.member.service.MemberService;

import javax.servlet.http.HttpSession;

/* 공통의 회원 클래스 컨트롤러 : 회원가입, 로그인, 회원 탈퇴 등을 구현 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;




    @PostMapping("/register")
    public ResponseEntity register(@RequestBody MemberDto.Post dto) {
        Member member = memberService.register(dto);
        return new ResponseEntity<>(member, HttpStatus.CREATED);
    }



    @PostMapping("/login")
    public ResponseEntity login(@RequestParam String email,
                                @RequestParam String password, HttpSession session) {
        Member member = memberService.login(email, password, session);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }



    @PostMapping("/logout")
    public ResponseEntity logout(HttpSession session) {
        memberService.logout(session);
        return ResponseEntity.ok().build();
    }




    @DeleteMapping("/{memberId}")
    public ResponseEntity deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.ok().build();
    }
}