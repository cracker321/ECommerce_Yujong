package yujong.ecommerce_yujong.security.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 이 클래스 SecurityConfig 를 이제 스프링 IoC 컨테이너가 관리하도록 해주는 어노테이션.
@EnableWebSecurity(debug = true)
//- '@EnableWebSecurity': '스프링 시큐리티 필터'를 '스프링 시큐리티 필터 체인'에 등록시키는 어노테이션.
//                        여기서 '스프링 시큐리티 필터'란 여기의 '클래스 SecurityConfig'를 말하는 것임.
@RequiredArgsConstructor
public class SecurityConfig {


    //사용자 인증 담당하는 커스텀 인증 프로바이더 주입받는 필드.
    private final SecurityProvider securityProvider;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // .headers().frameOptions().sameOrigin()
                // .and()
                .csrf().disable()
                //CSRF(Cross-Site Request Forgery) 공격을 방지하는 기능을 비활성화홤
                //즉, 이 설정은 웹 애플리케이션이 CSRF 토큰을 요구하지 않도록 함.
                .cors()
                //CORS(Cross-Origin Resource Sharing) 설정을 활성화함.
                //다른 도메인에서의 요청을 허용하고 관리함.
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //세션 관리 전략으로 'STATELESS'를 설정함.
                //세션을 생성하지 않고 상태를 유지하지 않는다는 의미임. 대신, 각 요청은 독립적으로 처리됨.
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                //'폼 기반 로그인 formLogin'과 'HTTP Basic 인증 기능'을 모두 비활성화시킴.
                .apply(new JwtSecurityConfig(securityProvider))
                //'JwtSecurityConfig 클래스'를 적용하여 JWT(Json Web Token) 기반 보안 설정을 적용함.
                .and()
                .authorizeHttpRequests(authorize -> authorize
                //클라이언트의 접속 URL 요청별로 개별 클라이언트의 권한에 맞춰 URL 별 접근 권한을 설정하는 메소드임.
                //아래에 이어지는 내용들을 총괄하는 설정임.

                        /* [ 메인 페이지는 모두 접근이 가능해야한다 ] */
                        //클라이언트로부터 넘어온 Json 데이터가 http GET 방식이면서, 
                        //URL 경로 '/'로 들어오는 클라이언트의 URL 접속 요청은 모든 사용자에게 허용해줌.
                        //즉, 메인페이지는 모두가 접속 가능.
                        .antMatchers(HttpMethod.GET, "/").permitAll() /* 메인 페이지 */

                        /* [ 회원 관련 접근 제한 ] */
                        .antMatchers(HttpMethod.POST, "/members/signup").permitAll() /* 자체 회원가입 */
                        //클라이언트로부터 넘어온 Json 데이터가 http POST 방식이면서,
                        //URL 경로 '/members/signup'으로 들어오는 클라이언트의 URL 접속 요청은 모든 사용자에게 허용해줌.
                        //즉, 자체 회원가입 페이지는 모두가 접속 가능.
                        .antMatchers(HttpMethod.POST, "/login").permitAll() /* 자체 로그인 */
                        .antMatchers(HttpMethod.GET, "/login/**").permitAll() /* 소셜 로그인을 위해 */
                        //클라이언트로부터 넘어온 Json 데이터가 http GET 방식이면서,
                        //URL 경로가 '/login/'으로 시작하는 클라이언트의 URL 접속 요청은 모든 사용자에게 허용해줌.
                        .antMatchers(HttpMethod.POST, "/login/**").permitAll() /* 소셜 로그인을 위해 */
                        .antMatchers(HttpMethod.GET, "/members/client/**").hasRole("CUSTOMER") /* 소비자 정보 조회 */
                        //클라이언트로부터 넘어온 Json 데이터가 http GET 방식이면서,
                        //URL 경로가 '/client/'로 시작하는 클라이언트의 URL 접속 요청은
                        //'CUSTOMER 역할'을 가진 사용자에게만 허용함.
                        .antMatchers(HttpMethod.PUT, "/members/client/**").hasRole("CUSTOMER") /* 소비자 정보 수정 */
                        .antMatchers(HttpMethod.GET,"/members/seller/**").permitAll() /* 생산자 정보 조회 -> 모두가 조회 가능 */
                        .antMatchers(HttpMethod.PUT, "/members/seller/**").hasRole("SELLER") /* 생산자 정보 수정 */
                        .antMatchers(HttpMethod.DELETE, "/members/**").hasAnyRole("CUSTOMER", "SELLER") /* 회원 탈퇴 */
                        //클라이언트로부터 넘어온 Json 데이터가 http DELETE 방식이면서,
                        //URL 경로가 '/members/'로 시작하는 클라이언트의 URL 접속 요청은
                        //'CUSTOMER 역할' 또는 'SELLER 역할'을 가진 사용자에게만 허용함.
                        .antMatchers(HttpMethod.GET, "/access").permitAll() /* 새로고침 */
                        .antMatchers(HttpMethod.GET, "/mypage/sold/*").permitAll() /* 생산자 게시판 조회 */
                        .antMatchers(HttpMethod.GET, "/mypage/*").hasRole("CUSTOMER") /* 구매자 주문내역 조회 */

                        /* [ 소셜 수정 권한 접근 ] */
                        .antMatchers(HttpMethod.PUT, "/social/**").hasRole("SOCIAL") /* 만약 소셜 권한을 부여할 경우 */

                        /* [ 판매 게시판 관련 접근 제한 ] */
                        .antMatchers(HttpMethod.GET, "/boards").permitAll() /* 판매 게시판 조회 */
                        .antMatchers(HttpMethod.GET, "/boards/**").permitAll() /* 판매 게시판 세부 조회 */
                        .antMatchers(HttpMethod.POST, "/boards").hasRole("SELLER") /* 판매 게시판 등록 */
                        .antMatchers(HttpMethod.PATCH, "/boards/*").hasRole("SELLER") /* 판매 게시판 수정 */
                        .antMatchers(HttpMethod.DELETE, "/boards/*").hasRole("SELLER") /* 판매 게시판 삭제*/

                        /* [ 리뷰 관련 접근 제한 ] */
                        // .antMatchers(HttpMethod.GET, "/boards/reviews/**").permitAll() /* 리뷰 조회 -> 판매 게시판 세부 조회 덕분에 없어도 됨. */
                        .antMatchers(HttpMethod.POST, "/boards/*/reviews").hasRole("CUSTOMER") /* 리뷰 등록 */
                        // .antMatchers(HttpMethod.PATCH, "/boars/reviews/**").hasRole("CUSTOMER") /* 리뷰 수정 */
                        .antMatchers(HttpMethod.DELETE, "/boards/reviews/**").hasRole("CUSTOMER") /* 리뷰 삭제 */

                        /* [ 주문 관련 접근 제한 ] */
                        .antMatchers(HttpMethod.POST, "/orders").hasRole("CUSTOMER") /* 주문 등록 */

                        /* [ 결제 관련 접근 제한 ] */
                        .antMatchers(HttpMethod.GET, "/order/pay/completed").permitAll() /* 결제 승인 요청 */
                        .antMatchers(HttpMethod.GET, "/order/pay/cancel").permitAll() /* 결제 취소 */
                        .antMatchers(HttpMethod.GET, "/order/pay/fail").permitAll() /* 결제 실패 */
                        .antMatchers(HttpMethod.DELETE, "/orders/**").permitAll() /* 주문 삭제 -> 결제 취소 혹은 실패시 작동 */
                        .antMatchers(HttpMethod.GET, "/orders/*").permitAll() /* 결제 성공 -> 주문 내역 조회 필요 */
                        .antMatchers(HttpMethod.GET, "/order/pay/*").hasRole("CUSTOMER") /* 결제 요청 */

                        /* [ 문의 관련 접근 제한 ] */
                        //- 판매자 SELLER 와 고객 CUSTOMER 둘 다 모두 문의를 조회, 등록, 수정, 삭제할 수 있어야 함.
                        .antMatchers(HttpMethod.GET, "/comments/*").permitAll() /* 문의 조회 */
                        .antMatchers(HttpMethod.POST, "/comments").hasAnyRole("SELLER", "CUSTOMER") /* 문의 등록 */
                        .antMatchers(HttpMethod.PATCH, "/comments/**").hasAnyRole("SELLER", "CUSTOMER") /* 문의 수정 */
                        .antMatchers(HttpMethod.DELETE, "/comments/**").hasAnyRole("SELLER", "CUSTOMER") /* 문의 삭제 */

                        /* [ 혹시 모르는 options 발생 시 허용 ]*/
                        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        /* [ 그 외 나머지 모든 URL 요청에 대해서는 웹사이트 내의 모든 페이지 접근을 허용함 ] */
                        .anyRequest().permitAll()
                )
                .exceptionHandling()
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint())
                .accessDeniedHandler(new MemberAccessDeniedHandler());

                /*
                [ 스프링시큐리티 내장 메소드 exceptionHandling() ]

                - 인증 및 권한 에러 처리를 담당함.
                  즉, 스프링 시큐리티가 예외 상황을 처리할 방법을 설정하는 데 사용되는 것임.
                  이 메소드를 호출하면 Exception HandlingConfigurer 라는 객체가 반환되고,
                  이 객체의 메소드들을 통해 다양한 예외 처리 전략을 설정할 수 있음.
                - 이 메소드에는 두 가지 주요 컴퍼넌트, 즉 AuthenticationEntryPoint와 AccessDeniedHandler가 있음.


                < AuthenticationEntryPooint >
                - '인증되지 않은 사용자 unauthenticated user'가 접근이 제한된 리소스(페이지 등)에 접근하려고 할 때 호출됨.
                  e.g) 로그인되지 않은 사용자가 MyPage에 접근하려고 하는 경우에, 그 사용자를 다시 로그인 페이지로 리디렉션해서
                       돌려보냄.
                       또는 그 사용자에게 인증 오류 응답을 생성해서 보여줌.
                - 개발자가 별도의 '사용자 정의 핸들러 MemberAuthenticationEntryPoint()'를 만들고,
                  그 내부에 이 경우에 맞게 적절하게 응답할 내용을 코드로 작성함.
                
                
                < AccessDeniedHandelr >
                - 권한이 부족한 사용자의 접근을 처리하는 커스텀 접근 거부 핸들러를 설정함.
                - 이미 인증된 사용자이지만, 자신의 권한으로는 접근하지 못하는 리소스(페이지 등)에 접근하려고 할 때 호출되어,
                  그 사용자를 어떻게 처리할지를 결정해주는 메소드.
                - 개발자가 별도의 '사용자 정의 핸들러 MemberAccessDeniedHandler()'를 만들고,
                  그 내부에 이 경우에 맞게 적절하게 응답할 내용을 코드로 작성함.

                 */
        
        

        return http.build();
        //위에서 작성한 스프링시큐리티 설정을 마무리하고, 해당 설정을 적용 빌드하여 HTTP 보안을 구성하는 부분.
        //이 코드는 시큐리티 설정이 모두 완료되었음을 의미하며, 이를 통해 위에서 작성한 보안 설정들이 실제로 적용됨.
        //- 'http': '시큐리티 내장 객체 HttpSecurity'를 참조하는 변수 http.
        //          '시큐리티 내장 클래스 HttpSecurity'는 스프링 시큐리티의 구성을 정의하는 데 사용되는 핵심 클래스임.
        //- 'build()': 이 클래스 맨 앞 쪽에 있는 'http.cors. ...'으로 체인 방식으로 작성하여 이어지는 설정을 이제 빌드하여
        //             HTTP 보안을 구성해줌. 즉, 이제 .build()를 통해 최종 완성 작품인 '시큐리티 필터 체인'을 생성한 것임.
        //             즉, 위에서 작성한 모든 정책들로부터 최종적인 '보안 필터 체인 객체 HttpSecurity 객체'를 생성한 것임.
        //             그리고 이것을 이제 최종적으로 이 SecurityConfig 클래스의 리턴값으로 반환해줌.


//=============================================================================================================


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    private final MemberPrincipalService memberPrincipalService;
    private final RefreshTokenRepository refreshTokenRepository;

    private void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberPrincipalService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public HttpSessionOAuth2AuthorizationRequestRepository oAuth2AuthorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }
}
