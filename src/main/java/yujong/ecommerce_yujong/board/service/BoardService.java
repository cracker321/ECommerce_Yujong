package yujong.ecommerce_yujong.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yujong.ecommerce_yujong.board.entity.Board;
import yujong.ecommerce_yujong.member.entity.Seller;
import yujong.ecommerce_yujong.member.service.SellerService;

@RequiredArgsConstructor
@Transactional(readOnly=true)
@Service
public class BoardService {

    private final SellerService sellerService;

//  < DB에 현재 존재하는 판매자 Seller 인지 여부를 확인하고, 존재한다면 그 판매자 Seller를 가져와서 반환해주고,
//    아니라면, Optional로 처리해서 내가 지정한 사용자 정의 에러 ExceptionCode.MEMBER_NOT_FOUND 를 발생시켜줌. >
    public Board createBoard(Board board, Long sellerId){

        Seller findSeller = sellerService.findVerifiedSeller(sellerId);
        
        































    }

}
