package yujong.ecommerce_yujong.security.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/* CORS 설정을 위한 @Configuration */
@Configuration // 이제 이 클래스 WebMvcConfig를 스프링 IoC 컨테이너가 되게 하기 위함.
public class WebMvcConfig implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);
    }
}
