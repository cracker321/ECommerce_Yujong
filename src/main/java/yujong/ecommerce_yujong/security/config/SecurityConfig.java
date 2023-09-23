package yujong.ecommerce_yujong.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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
                /* 메인 페이지는 모두 접근이 가능해야한다. */
                .antMatchers(HttpMethod.GET, "/").permitAll() /* 메인 페이지 */

                /* 회원 관련 접근 제한 */
                .antMatchers(HttpMethod.POST, "/members/signup").permitAll() /* 자체 회원가입 */
                .antMatchers(HttpMethod.POST, "/login").permitAll() /* 자체 로그인 */
                .antMatchers(HttpMethod.GET, "/login/**").permitAll() /* 소셜 로그인을 위해 */
                .antMatchers(HttpMethod.POST, "/login/**").permitAll() /* 소셜 로그인을 위해 */
                .antMatchers(HttpMethod.GET, "/members/customer/**").hasRole("CUSTOMER") /* 소비자 정보 조회 */
                .antMatchers(HttpMethod.PUT, "/members/customer/**").hasRole("CUSTOMER") /* 소비자 정보 수정 */
                .antMatchers(HttpMethod.GET,"/members/seller/**").permitAll() /* 생산자 정보 조회 -> 모두가 조회 가능 */
                .antMatchers(HttpMethod.PUT, "/members/seller/**").hasRole("SELLER") /* 생산자 정보 수정 */
                .antMatchers(HttpMethod.DELETE, "/members/**").hasAnyRole("CUSTOMER", "SELLER") /* 회원 탈퇴 */
                .antMatchers(HttpMethod.GET, "/access").permitAll() /* 새로고침 */
                .antMatchers(HttpMethod.GET, "/mypage/sold/*").permitAll() /* 생산자 게시판 조회 */
                .antMatchers(HttpMethod.GET, "/mypage/*").hasRole("CUSTOMER") /* 구매자 주문내역 조회 */


                /* 판매 게시판 관련 접근 제한 */
                .antMatchers(HttpMethod.GET, "/boards").permitAll() /* 판매 게시판 조회 */
                .antMatchers(HttpMethod.GET, "/boards/**").permitAll() /* 판매 게시판 세부 조회 */
                .antMatchers(HttpMethod.POST, "/boards").hasRole("SELLER") /* 판매 게시판 등록 */
                .antMatchers(HttpMethod.PATCH, "/boards/*").hasRole("SELLER") /* 판매 게시판 수정 */
                .antMatchers(HttpMethod.DELETE, "/boards/*").hasRole("SELLER") /* 판매 게시판 삭제*/

                /* 주문 관련 접근 제한 */
                .antMatchers(HttpMethod.POST, "/orders").hasRole("CUSTOMER") /* 주문 등록 */


                /* 문의 관련 접근 제한 */
                .antMatchers(HttpMethod.GET, "/comments/*").permitAll() /* 문의 조회 */
                .antMatchers(HttpMethod.POST, "/comments").hasAnyRole("SELLER", "CUSTOMER") /* 문의 등록 */
                .antMatchers(HttpMethod.PATCH, "/comments/**").hasAnyRole("SELLER", "CUSTOMER") /* 문의 수정 */
                .antMatchers(HttpMethod.DELETE, "/comments/**").hasAnyRole("SELLER", "CUSTOMER") /* 문의 삭제 */

                /* 혹시 모르는 options 발생 시 허용 */
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()


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