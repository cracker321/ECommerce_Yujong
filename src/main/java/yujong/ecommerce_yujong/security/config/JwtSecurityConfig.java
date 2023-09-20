package yujong.ecommerce_yujong.security.config;


import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import yujong.ecommerce_yujong.security.jwt.SecurityProvider;
import yujong.ecommerce_yujong.security.jwt.filter.SecurityFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    /*
    [ SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> ]

    - 스프링 시큐리티의 기본 설정을 확장하고 사용자 정의 설정을 적용하는 클래스.


     */
    private final SecurityProvider securityProvider;


    @Override
    public void configure(HttpSecurity http) {
        SecurityFilter customFilter = new SecurityFilter(securityProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }

}   
