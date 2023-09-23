package yujong.ecommerce_yujong.member.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class SellerPatchDto {

    private String name;

    private long sellerId;

    private String phone;

    private String password;

    private String address;

    private String introduction;

}