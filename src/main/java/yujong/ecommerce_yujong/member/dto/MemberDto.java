package yujong.ecommerce_yujong.member.dto;

import lombok.*;
import yujong.ecommerce_yujong.member.role.Role;


@Data
public class MemberDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Post {

        private String email;

        /* 이름은 공백이 아니어야 한다. */
        private String name;

        /* 비밀번호 형식을 지켜서 작성해야 한다. */
        private String password;

        /* 휴대 전화 번호는 형식을 지켜서 작성해야 한다.*/
        private String phone;

        /* 주소는 공백이 아니어야 한다. */
        private String address;

        private Role role;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CustomerResponseDto{
        private long memberId;
        private long customerId;
        private String email;
        private String name;
        private String phone;
        private String address;
        private Role role;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SellerResponseDto{
        private long memberId;
        private long sellerId;
        private String email;
        private String name;
        private String phone;
        private String address;
        private Role role;
        private String introduce;
        private String imageUrl;
    }
}
