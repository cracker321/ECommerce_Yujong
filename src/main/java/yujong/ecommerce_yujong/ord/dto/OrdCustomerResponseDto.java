package yujong.ecommerce_yujong.ord.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdCustomerResponseDto { //구매 내역 조회

    private Long ordId;

    private Long clientId;

    private Long boardId;

    private String title;

    private String name;

    private String address;

    private String phone;

    private int quantity;
}
