package yujong.ecommerce_yujong.참고사항;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yujong.ecommerce_yujong.member.entity.Seller;
import yujong.ecommerce_yujong.member.repository.SellerRepository;

public class Optional {



    /*

    [ Optional.ofNullalbe(인수).ifPresent(인수) ]

    - 'Optional.ofNullable(인수)'
      : DB를 조회해본 후에, 주어진 인수에 해당하는 데이터가 DB에 없는 경우(null)라면, Optional.empty() 객체를 반환하고,
        이 때 반환된 Optional.empty() 객체를 예외 처리 해주기 위해,
        이 반환된 객체를 참조하는 변수에 대해 그 아래 줄에 반드시 orElseThrow를 작성해줘야 한다!
        만약 DB를 조회해본 휑, 주어진 인수에 해당하는 데이터가 DB에 있는 경우라면, 
        당연히 이 때 주어진 인수를 감싸고 있는 Optional 객체를
        그리고 이 때도, 반환된 Optional 객체에서 Optional 을 벗겨내기 위해 orElseThrow 를 위에서와 같이 작성해줘야 함.

     - 'ifPresent(사용자 정의 변수명(아무거나 해도 되. a, b, c 등등) -> action)'
       : Optional.ofNullable(인수)에 이어져서 호출되는 Optional 클래스의 내장 메소드이며,
         Optional.ofNullable(인수)의 '인수'가 DB에 존재하는 경우 그 인수를 조회해서 가져오고,
         그 db로부터 가져온 인수를 감싸고 있는 Optional 객체가 반환되는데,
         그 db로부터 가져온 인수를 감싸고 있는 Optional 객체를 인수로 받아들이고, 여기서 '사용자 정의 변수명'이
         그 db로부터 가져온 인수를 감싸고 있는 Optional 객체를 참조하고,
         그 변수를 '->' 를 통해 뒤이어 이어진 람다식 안의 action 로직에서 사용하는 과정이 되는 것임.

     */


//    @RequiredArgsConstructor
//    @Transactional(readOnly=true)
//    @Service
//    public class SellerService {
//
//        private final SellerRepository sellerRepository;
//

//        //< DB에 현재 존재하는 판매자 Seller 인지 여부를 확인하고, 존재한다면 그 판매자 Seller를 가져와서 반환해주고,
//            아니라면, Optional로 처리해서 내가 지정한 사용자 정의 에러 ExceptionCode.MEMBER_NOT_FOUND 를 발생시켜줌. >
//        public Seller findVerifiedSeller(long sellerId) {
//
//ㄴ
//            //- 'sellerRepository.findById(sellerId)'
//            //   : DB로부터 판매자 Seller 객체를 findById를 통해 가져옴
//            //- 'Optional<Seller> optionalSeller'
//            //   : 그런데, 외부에서 인자로 들어온 sellerId값에 해당하는 판매자 Seller가 DB에 있을 수도 있고, 없을 수도 있음.
//            //   따라서, Optional 타입으로 감싸주는 것임.
//            //   Optional 타입으로 감싸주게 되면,
//            //   만약, 외부에서 인자로 들어온 id값에 해당하는 판매자 Seller가 DB에 있으면,
//            //   당연히, 그 판매자 Seller 객체를 DB로부터 가져와서 '그 판매자 Seller 객체를 반환'해주고,
//            //   없으면(null 상황이면), Optional.empty() 즉, Optional 빈 객체를 반환한다.
//            //   Optional 빈 객체를 반환하는 경우, SellerRepository를 호출한 여기 SellerService의 그 호출한 부분 바로 아래에
//            //   이 SellerRepository를 호출한 부분 아래에 반드시 null을 처리해주는 orElseThrow을 작성해줘야 한다!!


//            //- 'optionalSeller.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOW_FOUND));
//            //  : 윗 라인에서 감싼 Optional 객체 안에 db로부터 조회해서 가져온 판매자 Seller 객체가 있으면
//            //    당연히 그 판매자 Seller 객체를 반환해주고,
//            //    만약 외부에서 들어온 판매자 id에 해당하는 판매자 Seller 객체가 없어서 가져오지 못하는 경우 즉, null인 경우라면
//            //    개발자인 내가 지정한 사용자 예외인  'global 디렉토리 안에 작성한 클래스 BusinessLogicException'를 기반으로
//            //    BusinessLogicException 객체를 생성해서 그 BusinessLogicException 객체의 필드 중 하나인
//            //    ExceptionCode.MEMBER_NOT_FOUND 를 반환해준다!!
//            //- 'Seller findSeller'
//            //  : 윗 라인에서 감싼 Optional 객체 안에 db로부터 조회해서 가져온 판매자 Seller 객체가 있으면
//            //    당연히 그 판매자 Seller 객체를 반환해서 변수 findSeller 에 담는다!
//            //- 'return findSeller'
//            //  : 이 메소드 findVerifiedSeller로 외부로부터 들어온 sellerId에 해당하는 그 판매자 Seller를 DB에서 찾아 가져와서
//            //    이 메소드의 반환값으로 최종 반환해준다!


//            Optional<Seller> optionalSeller = sellerRepository.findById(sellerId);
//            Seller findSeller = optionalSeller.orElseThrow(() ->
//                                  new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
//
//
//            return findSeller;



//        /*
//        JpaRepository의 내장메소드 findById는 사실 원래 아래와 같음
//
//        < JpaRepository를 상속받은 내장 레펏 MemberRepository >
//
//        public interface MemberRepository extends JpaRepository<Member, Long> {
//
//
//        }
//
//
//        < JpaRepository를 상속받은 내장 레펏 MemberRepository의 보이지는 않지만 실제 내장되어 있는 부분의 CRUD 로직(메소드) >
//
//                //< 1. DB로부터 회원 Member (객체)를 조회해서 가져오기 >
//                public Member findById(Long id){
//
//                    Member member = em.find(Member.class, id);
//
//                    return member;
//                }
//
//         */
//
//
//        }
//    }

}
