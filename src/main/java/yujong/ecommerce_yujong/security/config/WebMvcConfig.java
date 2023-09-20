package yujong.ecommerce_yujong.security.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/* CORS 설정을 위한 @Configuration */
@Configuration // 이제 이 클래스 WebMvcConfig를 스프링 IoC 컨테이너가 되게 하기 위함.
public class WebMvcConfig implements WebMvcConfigurer {


    /*

    [ CORS ]

    https://k3068.tistory.com/51





     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("POST", "PUT", "GET", "DELETE", "OPTIONS", "PATCH") /* 요청 가능한 메서드 */
                .allowedHeaders("*") /* 헤더 허용 */
                .exposedHeaders("Authorization", "Refresh") /* 헤더를 통하여 토큰을 전달해야 하기 때문, 추가 헤더 허용 */
                .allowedOriginPatterns("*")
                // .maxAge(3600) /* pre flight 요청에 대한 응답을 캐싱하는 시간 */
                .allowCredentials(true); /* 쿠키 요청 허용 */
    }
}
