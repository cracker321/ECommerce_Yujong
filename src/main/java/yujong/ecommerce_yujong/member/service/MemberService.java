package yujong.ecommerce_yujong.member.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yujong.ecommerce_yujong.global.exception.BusinessLogicException;
import yujong.ecommerce_yujong.global.exception.ExceptionCode;
import yujong.ecommerce_yujong.member.entity.Customer;
import yujong.ecommerce_yujong.member.entity.Member;
import yujong.ecommerce_yujong.member.entity.Seller;
import yujong.ecommerce_yujong.member.repository.MemberRepository;
import yujong.ecommerce_yujong.member.role.Role;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Transactional(readOnly=true)
@Service
public class MemberService implements UserDetailsService {


    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(member.getRole().name())); //권한 부여

        return new User(member.getEmail(), member.getPassword(), authorities);

    }



    /* 회원 Member 조회 Read */
    /* DB에 존재하는 회원 Member 인지 여부 확인 */
   public Member findVerifiedMember(long memberId){

       Member findMember = memberRepository.findById(memberId)
               .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

       return findMember;
   }






    /* 회원 Member 가입 Create */
    public Member createMember(Member member) {


        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }

        /* 회원가입할 때 입력하는 비밀번호를 암호화해서 db에 저장 */
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encodedPassword);

        if (member.getRole() == Role.SELLER) {

            member.setSeller(new Seller());
            member.getSeller().setIntroduction("안녕하세요, " + member.getName() + "입니다.");


        } else if (member.getRole() == Role.CUSTOMER) {

            /* 여기에 회원가입하고자 하는 고객 Customer 에 관한 추가 회원 정보를 입력 가능*/
            member.setCustomer(new Customer());
        } else {
            throw new BusinessLogicException(ExceptionCode.ROLE_ERROR);
        }


        member.setRole(member.getRole());

        return memberRepository.save(member);
    }





    /* 회원 Member 정보 수정 Update */
    public Member updateMember(long memberId, Member member) {


        Member findMember = findVerifiedMember(memberId);


        Member existingMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        existingMember.setName(member.getName());  // 예시: 이름 변경
        // 다른 필드도 여기에서 변경 가능
        return memberRepository.save(existingMember);
    }





    /* 회원 Member 삭제 Delete */
    public void deleteMember(long memberId){

       Member member = findVerifiedMember(memberId);

       memberRepository.delete(member);
    }

}

