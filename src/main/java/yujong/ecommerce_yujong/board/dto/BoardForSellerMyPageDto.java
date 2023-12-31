package yujong.ecommerce_yujong.board.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardForSellerMyPageDto {
    private long boardId;

    private String title;               //게시글 제목

    private int stock;                  //초기 상품 재고

    private int leftStock;              //잔여 재고

    private LocalDate createdAt;    //게시글 생성 시간
}