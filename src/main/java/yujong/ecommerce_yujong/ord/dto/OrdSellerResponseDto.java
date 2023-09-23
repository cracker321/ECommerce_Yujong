package yujong.ecommerce_yujong.ord.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdSellerResponseDto { //판매 내역 조회

    private Long boardId;

    private Long sellerId;

    private String title;

    private String name;

    private int price;

    private String phone;

    private int stock;

    private int leftStock;

    private LocalDate createAt;

    private LocalDate modifiedAt;
}