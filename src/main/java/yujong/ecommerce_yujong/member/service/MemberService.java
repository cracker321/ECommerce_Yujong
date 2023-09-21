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

    public Member register(MemberDto.Post request) {
        if(memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = new Member();
        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setPassword(encodedPassword);
        //member.setPhone(request.getPhone());
        member.setAddress(request.getAddress());
        member.setRole(request.getRole());
        // Initialize other fields like socialId, providerType, etc. as null or default

        return memberRepository.save(member);
    }


    public Member updateMember(long memberId, Member member) {
        Member existingMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        existingMember.setName(member.getName());  // 예시: 이름 변경
        // 다른 필드도 여기에서 변경 가능
        return memberRepository.save(existingMember);
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

