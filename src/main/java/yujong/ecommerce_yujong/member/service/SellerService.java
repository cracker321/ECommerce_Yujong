package yujong.ecommerce_yujong.member.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yujong.ecommerce_yujong.board.dto.BoardForSellerMyPageDto;
import yujong.ecommerce_yujong.board.repository.BoardRepository;
import yujong.ecommerce_yujong.global.exception.BusinessLogicException;
import yujong.ecommerce_yujong.global.exception.ExceptionCode;
import yujong.ecommerce_yujong.member.entity.Seller;
import yujong.ecommerce_yujong.member.repository.SellerRepository;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Transactional(readOnly=true)
@Service
public class SellerService {

    private final SellerRepository sellerRepository;
    private final BoardRepository boardRepository;
    private final MemberService memberService;


//=============================================================================================================


//  [ DB에 현재 존재하는 판매자 Seller 인지 여부를 확인하고, 존재한다면 그 판매자 Seller를 가져와서 반환해주고,
//    아니라면, Optional로 처리해서 내가 지정한 사용자 정의 에러 ExceptionCode.MEMBER_NOT_FOUND 를 발생시켜줌. ]
    public Seller findVerifiedSeller(long sellerId) {

        Optional<Seller> optionalSeller = sellerRepository.findById(sellerId);
        Seller findSeller = optionalSeller
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));


        return findSeller;



    }


//=============================================================================================================

    //[ 판매자 Seller 조회 ]
    //- 조회해서 가져오기

    public Seller findSeller(long sellerId) {

        Seller seller = findVerifiedSeller(sellerId);

        return seller;
    }


//=============================================================================================================


    public List<BoardForSellerMyPageDto> getSellerBoard(long sellerId) {
        List<BoardForSellerMyPageDto> sellerBoard = boardRepository.sellerBoard(sellerId);

        if (sellerBoard.size() > 5) {
            sellerBoard = sellerBoard.subList(0, 5);
        }

        return sellerBoard;


    }
}
