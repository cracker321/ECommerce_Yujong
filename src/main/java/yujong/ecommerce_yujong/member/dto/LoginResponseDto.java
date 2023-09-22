package yujong.ecommerce_yujong.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yujong.ecommerce_yujong.member.role.Role;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private Long memberId;
    private String email;
    private String name;
    private Role role;
}
