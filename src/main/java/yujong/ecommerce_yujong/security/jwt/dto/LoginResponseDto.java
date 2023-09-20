package yujong.ecommerce_yujong.security.jwt.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    public static class Member{
        private long memberId;
        private String name;
        private String role;

        /* 소셜 로그인 시, 바로 url에서 작업할 경우 헤더에서 가져오기 힘들어서 우선 바디에 출력 중. */
        private String authorization;
    }

    public static class Customer{
        private long memberId;
        private long customerId;
        private String name;
        private String role;
    }

    public static class Seller{
        private long memberId;
        private long sellerId;
        private String name;
        private String role;
    }
}