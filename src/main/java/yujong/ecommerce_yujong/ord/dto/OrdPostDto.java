package yujong.ecommerce_yujong.ord.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdPostDto {

    private Long clientId;

    private Long boardId;

    private String address;

    private String phone;

    private int quantity;

    private int totalPrice;

}
