package yujong.ecommerce_yujong.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yujong.ecommerce_yujong.member.dto.MemberDto;
import yujong.ecommerce_yujong.member.entity.Member;
import yujong.ecommerce_yujong.member.service.MemberService;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/* 공통의 회원 클래스 컨트롤러 : 회원가입, 로그인, 회원 탈퇴 등을 구현 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody MemberDto.Post request) {
        try {
            Member member = memberService.register(request);
            return new ResponseEntity<>(member, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestParam String email,
                                   @RequestParam String password,
                                   HttpSession session) {
        Optional<Member> member = Optional.ofNullable(memberService.login(email, password, session));
        if (member.isPresent()) {
            return new ResponseEntity<>(member, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpSession session) {
        memberService.logout(session);
        return new ResponseEntity<>("Logged out", HttpStatus.OK);
    }



    @DeleteMapping("/{memberId}")
    public ResponseEntity unregister(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return new ResponseEntity<>("Member removed", HttpStatus.OK);
    }
}