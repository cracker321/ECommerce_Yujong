package yujong.ecommerce_yujong.member.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerPatchDto {

    private String name;

    private String phone;

    private String password;

    private String address;

}