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



//=====================================================================================================================

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(member.getRole().name())); //권한 부여

        return new User(member.getEmail(), member.getPassword(), authorities);

    }

//=====================================================================================================================


    //[ DB에 현재 존재하는 회원 Member 인지 여부를 확인하고,
    // 존재한다면 그 회원 Member 를 조회 Read 해서 가져와서 반환해주고,
    // 아니라면, Optional 로 처리해서 내가 지정한 사용자 정의 에러 ExceptionCode.MEMBER_NOT_FOUND 를 발생시켜줌. ]

   public Member findVerifiedMember(long memberId){

       Member findMember = memberRepository.findById(memberId)
               .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

       return findMember;
   }




//=====================================================================================================================



    //[ 회원 Member 가입 Create ]

    //- Customer: 회원가입도 했고, 물건까지 구매한 고객
    //- Seller: 회원가입도 했고, 물건을 판매하는 판매자

    public Member createMember(Member member) {


        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }

        //회원가입할 때 입력하는 비밀번호를 암호화해서 db에 저장시킴.
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encodedPassword);

        if (member.getRole() == Role.SELLER) {

            member.setSeller(new Seller());
            //여기에 회원가입하고자 하는 판매자 Selelr 에 대한 추가 회원 정보를 입력할 수 있음.
            //이는 판매자 Seller 엔티티 클래스 내부에 있는 필드를 따라감.
            //만약, 판매자 Seller 엔티티 클래스 내부에 '필드 storeName', '필드 sellerGrade'와 같은 것이 있다면,
            //바로 여기에 아래처럼 추가할 수 있음.
            //e.g) seller.setStoreName("새마을식당");
            //     seller.setSellerGrade(1); //판매자 등급을 초기 등급인 1 등급으로 설정.
            //기본적으로는 새로운 회원이 가입할 때 입력한 Member 객체 내부의 필드 정보들이
            //이 메소드 createMember 의 인수로 넘어오고, 그 넘어온 정보들을 일단 기본적으로 이 회원을 판매자 Seller 로 인정해서
            //이 신규 가입 회원 Memember를 '판매자 Seller'로서 인정하고 신규회원가입 때 입력한 정보를
            //'판매자 Seller' 로서의 기본 정보로 DB에 넣는 것임.
            member.getSeller().setIntroduction("안녕하세요, " + member.getName() + "입니다.");


        } else if (member.getRole() == Role.CUSTOMER) {
            // 여기에 회원가입하고자 하는 고객 Customer 에 관한 추가 회원 정보를 입력할 수 있음.
            member.setCustomer(new Customer());
        } else {
            throw new BusinessLogicException(ExceptionCode.ROLE_ERROR);
        }


        member.setRole(member.getRole());

        return memberRepository.save(member);
    }





//=====================================================================================================================



    //[ 회원 Member 정보 수정 Update ]

    public Member updateMember(long memberId, Member member) {


        Member findMember = findVerifiedMember(memberId);


        Member existingMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        existingMember.setName(member.getName());  // 예시: 이름 변경
        // 다른 필드도 여기에서 변경 가능
        return memberRepository.save(existingMember);
    }




//=====================================================================================================================


    //[ 회원 Member 삭제 Delete ]
    public void deleteMember(long memberId){

       Member member = findVerifiedMember(memberId);

       memberRepository.delete(member);
    }





//=====================================================================================================================



//    //[ 회원 Member 로그인 ]
//
//    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
//        Member member = memberRepository.findByEmail(loginRequestDto.getEmail())
//                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
//
//        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
//            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
//        }
//
//
//        return new LoginResponseDto(
//                member.getMemberId(),
//                member.getEmail(),
//                member.getName(),
//                member.getRole()
//        );
//    }



//=====================================================================================================================



//    //[ 회원 Member 로그아웃 logout ]
//    public void logout(HttpSession session) {
//        session.invalidate(); // 세션 초기화
//    }





//=====================================================================================================================





//=====================================================================================================================

}

