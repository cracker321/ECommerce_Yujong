package yujong.ecommerce_yujong.member.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yujong.ecommerce_yujong.member.dto.MemberDto;
import yujong.ecommerce_yujong.member.entity.Member;
import yujong.ecommerce_yujong.member.repository.MemberRepository;

import javax.servlet.http.HttpSession;


@RequiredArgsConstructor
@Transactional(readOnly=true)
@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Member register(MemberDto.Post dto) {
        if (memberRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("이미 가입한 이메일입니다.");
        }
        Member member = Member.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .role(dto.getRole())
                .build();
        return memberRepository.save(member);
    }

    public Member login(String email, String password, HttpSession session) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원 정보가 없습니다."));
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new RuntimeException("비밀번호가 틀립니다.");
        }
        session.setAttribute("member", member);
        return member;
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }
}

