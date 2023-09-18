package yujong.ecommerce_yujong.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yujong.ecommerce_yujong.board.entity.Board;

@RequiredArgsConstructor
@Transactional(readOnly=true)
@Service
public class BoardService {

    public Board createBoard(Board board, Long sellerId){

        //DB에 해당 판매자 Seller의 판매자 id인 sellerId에 해당하는 판매자 존재 여부 확인해서,
        Seller findSeller = sellerService





























    }

}
