package yujong.ecommerce_yujong.board.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BoardPostDto {
    
    
    private Long sellerId;

    @NotBlank
    private String title; //게시글 Board 제목

    @NotBlank
    private String content; //게시글 Board 내용

    @NotBlank
    private int price; //상품 Product 가격

    @NotBlank
    private int stock; //상품 Product 재고

}
