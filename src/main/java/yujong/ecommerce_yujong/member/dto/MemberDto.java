package yujong.ecommerce_yujong.member.dto;

import lombok.*;


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

        /* 들어오는 값으로 Authority 부여 및 DB에 역할 저장 */
        private String role;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CustomerResponseDto{
        private long memberId;
        private long custoemrId;
        private String email;
        private String name;
        private String phone;
        private String address;
        private String role;
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
        private String role;
        private String introduce;
        private String imageUrl;
    }
}
