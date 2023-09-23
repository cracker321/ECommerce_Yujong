package yujong.ecommerce_yujong.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yujong.ecommerce_yujong.board.dto.BoardForSellerMyPageDto;
import yujong.ecommerce_yujong.global.response.MultiSellerResponseDto;
import yujong.ecommerce_yujong.member.dto.MemberDto;
import yujong.ecommerce_yujong.member.entity.Member;
import yujong.ecommerce_yujong.member.mapper.MemberMapper;
import yujong.ecommerce_yujong.member.service.SellerService;

import javax.validation.constraints.Positive;
import java.util.List;

/* 판매자 관련 컨트롤러 : 마이페이지 조회 */
@RestController
@Slf4j
@RequestMapping("/members/seller")
@RequiredArgsConstructor
public class SellerController {


    private final SellerService sellerService;
    private final MemberMapper memberMapper;



    /* 판매자 마이 페이지 조회 */
    @GetMapping("/{seller_id}")
    public ResponseEntity getSeller(@PathVariable("seller_id") @Positive long sellerId) {


        Member member = sellerService.findSeller(sellerId).getMember();

        List<BoardForSellerMyPageDto> boardList = sellerService.getSellerBoard(sellerId);

        MemberDto.SellerResponseDto response = memberMapper.memberToSellerDto(member);

        return new ResponseEntity<>(
                new MultiSellerResponseDto(response,boardList), HttpStatus.OK);
    }


}
