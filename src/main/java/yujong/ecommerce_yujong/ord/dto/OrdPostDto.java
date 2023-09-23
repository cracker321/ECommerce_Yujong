package yujong.ecommerce_yujong.ord.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdPostDto { // 주문 내역 그 자체 조회

    private Long customerId;

    private Long boardId;

    private String address;

    private String phone;

    private int quantity;

    private int totalPrice;

}
