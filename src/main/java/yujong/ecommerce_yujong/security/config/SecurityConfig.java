package yujong.ecommerce_yujong.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import yujong.ecommerce_yujong.member.service.MemberService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // CSRF 보호를 비활성화 (테스트 목적으로)
                .authorizeRequests()
                .antMatchers("/members/signup").permitAll()  // 회원가입 경로는 누구나 접근 가능
                .anyRequest().authenticated()  // 나머지 요청은 모두 인증된 사용자만 접근 가능
                .and()
                .formLogin()  // 로그인 설정
                .loginProcessingUrl("/members/login")  // 로그인 처리 URL
                .usernameParameter("email")  // 로그인 폼의 이메일 파라미터명
                .passwordParameter("password")  // 로그인 폼의 패스워드 파라미터명
                .defaultSuccessUrl("/")  // 로그인 성공시 이동할 URL
                .and()
                .logout()  // 로그아웃 설정
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/");  // 로그아웃 성공시 이동할 URL
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder);
    }
}