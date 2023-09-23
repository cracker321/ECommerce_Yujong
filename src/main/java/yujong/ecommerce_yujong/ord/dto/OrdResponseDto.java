package yujong.ecommerce_yujong.ord.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import yujong.ecommerce_yujong.ord.entity.Ord;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class OrdResponseDto {

    private Long ordId;

    private Long customerId;

    private Long boardId;

    private String name;

    private String address;

    private String phone;

    private int totalPrice;

    private int quantity;

    private Ord.OrdStatus ordStatus;

    private LocalDate createdAt;

}
