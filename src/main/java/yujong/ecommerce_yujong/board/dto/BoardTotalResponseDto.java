package yujong.ecommerce_yujong.board.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardTotalResponseDto {

    private Long boardId;

    private Long productId;

    private Long sellerId;

    private String name;            //판매자 이름

    private String title;           //게시글 제목

    private int price;              //상품 가격

    private int leftStock;          //잔여 재고

}
