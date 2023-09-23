package yujong.ecommerce_yujong.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class PasswordEncoderConfig {
    //MemberService 클래스와 SecurityConfig 클래스 간 순환참조 해결하기 위해
    //원래는 SecurityConfig 클래스 내부에 있던 아래 메소드 passwordEncoder를 SecurityConfig 에서 삭제하고
    //여기에 새롭게 클래스를 생성해서 아래 메소드를 넣음.


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}