package yujong.ecommerce_yujong.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class PasswordEncoderConfig {
    //순환참조 해결하기 위해 원래는 SecurityConfig 에 있던 아래 부분을 그곳에서 삭제하고
    //여기에 새롭게 클래스 생성함.


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}