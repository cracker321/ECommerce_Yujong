package yujong.ecommerce_yujong.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yujong.ecommerce_yujong.product.entity.Product;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardPatchDto {

    @NotBlank
    private Long boardId;

    @NotBlank
    private String mainImage; //게시판 썸네일 이미지

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private int price;

    @NotBlank
    private Product.ProductStatus status; //현재 상품 판매 상태

    @NotBlank
    private int category;


}
