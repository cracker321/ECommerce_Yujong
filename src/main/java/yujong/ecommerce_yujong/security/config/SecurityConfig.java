package yujong.ecommerce_yujong.security.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.web.SecurityFilterChain;
import yujong.ecommerce_yujong.security.jwt.SecurityProvider;
import yujong.ecommerce_yujong.security.jwt.handler.MemberAccessDeniedHandler;
import yujong.ecommerce_yujong.security.jwt.handler.MemberAuthenticationEntryPoint;

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
                // .headers().frameOptions().sameOrigin()
                // .and()
                http.csrf(Customizer.withDefaults())
                //CSRF(Cross-Site Request Forgery) 공격을 방지하는 기능을 비활성화홤
                //즉, 이 설정은 웹 애플리케이션이 CSRF 토큰을 요구하지 않도록 함.
                .cors(AbstractHttpConfigurer::disable)
                //CORS(Cross-Origin Resource Sharing) 설정을 활성화함.
                //다른 도메인에서의 요청을 허용하고 관리함.
                .and()
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
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
                        .requestMatchers(HttpMethod.GET, "/").permitAll() /* 메인 페이지 */

                        /* [ 회원 관련 접근 제한 ] */
                        .requestMatchers(HttpMethod.POST, "/members/signup").permitAll() /* 자체 회원가입 */
                        //클라이언트로부터 넘어온 Json 데이터가 http POST 방식이면서,
                        //URL 경로 '/members/signup'으로 들어오는 클라이언트의 URL 접속 요청은 모든 사용자에게 허용해줌.
                        //즉, 자체 회원가입 페이지는 모두가 접속 가능.
                        .requestMatchers(HttpMethod.POST, "/login").permitAll() /* 자체 로그인 */
                        .requestMatchers(HttpMethod.GET, "/login/**").permitAll() /* 소셜 로그인을 위해 */
                        //클라이언트로부터 넘어온 Json 데이터가 http GET 방식이면서,
                        //URL 경로가 '/login/'으로 시작하는 클라이언트의 URL 접속 요청은 모든 사용자에게 허용해줌.
                        .requestMatchers(HttpMethod.POST, "/login/**").permitAll() /* 소셜 로그인을 위해 */
                        .requestMatchers(HttpMethod.GET, "/members/client/**").hasRole("CUSTOMER") /* 소비자 정보 조회 */
                        //클라이언트로부터 넘어온 Json 데이터가 http GET 방식이면서,
                        //URL 경로가 '/client/'로 시작하는 클라이언트의 URL 접속 요청은
                        //'CUSTOMER 역할'을 가진 사용자에게만 허용함.
                        .requestMatchers(HttpMethod.PUT, "/members/client/**").hasRole("CUSTOMER") /* 소비자 정보 수정 */
                        .requestMatchers(HttpMethod.GET,"/members/seller/**").permitAll() /* 생산자 정보 조회 -> 모두가 조회 가능 */
                        .requestMatchers(HttpMethod.PUT, "/members/seller/**").hasRole("SELLER") /* 생산자 정보 수정 */
                        .requestMatchers(HttpMethod.DELETE, "/members/**").hasAnyRole("CUSTOMER", "SELLER") /* 회원 탈퇴 */
                        //클라이언트로부터 넘어온 Json 데이터가 http DELETE 방식이면서,
                        //URL 경로가 '/members/'로 시작하는 클라이언트의 URL 접속 요청은
                        //'CUSTOMER 역할' 또는 'SELLER 역할'을 가진 사용자에게만 허용함.
                        .requestMatchers(HttpMethod.GET, "/access").permitAll() /* 새로고침 */
                        .requestMatchers(HttpMethod.GET, "/mypage/sold/*").permitAll() /* 생산자 게시판 조회 */
                        .requestMatchers(HttpMethod.GET, "/mypage/*").hasRole("CUSTOMER") /* 구매자 주문내역 조회 */

                        /* [ 소셜 수정 권한 접근 ] */
                        .requestMatchers(HttpMethod.PUT, "/social/**").hasRole("SOCIAL") /* 만약 소셜 권한을 부여할 경우 */

                        /* [ 판매 게시판 관련 접근 제한 ] */
                        .requestMatchers(HttpMethod.GET, "/boards").permitAll() /* 판매 게시판 조회 */
                        .requestMatchers(HttpMethod.GET, "/boards/**").permitAll() /* 판매 게시판 세부 조회 */
                        .requestMatchers(HttpMethod.POST, "/boards").hasRole("SELLER") /* 판매 게시판 등록 */
                        .requestMatchers(HttpMethod.PATCH, "/boards/*").hasRole("SELLER") /* 판매 게시판 수정 */
                        .requestMatchers(HttpMethod.DELETE, "/boards/*").hasRole("SELLER") /* 판매 게시판 삭제*/

                        /* [ 리뷰 관련 접근 제한 ] */
                        // .requestMatchers(HttpMethod.GET, "/boards/reviews/**").permitAll() /* 리뷰 조회 -> 판매 게시판 세부 조회 덕분에 없어도 됨. */
                        .requestMatchers(HttpMethod.POST, "/boards/*/reviews").hasRole("CUSTOMER") /* 리뷰 등록 */
                        // .requestMatchers(HttpMethod.PATCH, "/boars/reviews/**").hasRole("CUSTOMER") /* 리뷰 수정 */
                        .requestMatchers(HttpMethod.DELETE, "/boards/reviews/**").hasRole("CUSTOMER") /* 리뷰 삭제 */

                        /* [ 주문 관련 접근 제한 ] */
                        .requestMatchers(HttpMethod.POST, "/orders").hasRole("CUSTOMER") /* 주문 등록 */

                        /* [ 결제 관련 접근 제한 ] */
                        .requestMatchers(HttpMethod.GET, "/order/pay/completed").permitAll() /* 결제 승인 요청 */
                        .requestMatchers(HttpMethod.GET, "/order/pay/cancel").permitAll() /* 결제 취소 */
                        .requestMatchers(HttpMethod.GET, "/order/pay/fail").permitAll() /* 결제 실패 */
                        .requestMatchers(HttpMethod.DELETE, "/orders/**").permitAll() /* 주문 삭제 -> 결제 취소 혹은 실패시 작동 */
                        .requestMatchers(HttpMethod.GET, "/orders/*").permitAll() /* 결제 성공 -> 주문 내역 조회 필요 */
                        .requestMatchers(HttpMethod.GET, "/order/pay/*").hasRole("CUSTOMER") /* 결제 요청 */

                        /* [ 문의 관련 접근 제한 ] */
                        //- 판매자 SELLER 와 고객 CUSTOMER 둘 다 모두 문의를 조회, 등록, 수정, 삭제할 수 있어야 함.
                        .requestMatchers(HttpMethod.GET, "/comments/*").permitAll() /* 문의 조회 */
                        .requestMatchers(HttpMethod.POST, "/comments").hasAnyRole("SELLER", "CUSTOMER") /* 문의 등록 */
                        .requestMatchers(HttpMethod.PATCH, "/comments/**").hasAnyRole("SELLER", "CUSTOMER") /* 문의 수정 */
                        .requestMatchers(HttpMethod.DELETE, "/comments/**").hasAnyRole("SELLER", "CUSTOMER") /* 문의 삭제 */

                        /* [ 혹시 모르는 options 발생 시 허용 ]*/
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

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
        //             즉, '시큐리티 내장 객체 HttpSecurity'를 바탕으로 하여,
        //             위에서 작성한 모든 정책들로부터 최종적인 '보안 필터 체인 객체 HttpSecurity 객체'를 생성한 것임.
        //             그리고 이것을 이제 최종적으로 이 SecurityConfig 클래스의 리턴값으로 반환해줌.




//=============================================================================================================

    /*
    [ 스프링 시큐리티 내장 인터페이스 PasswordEncoder 의 구현체를 @Bean 으로 등록 ]

    - 새롭게 회원가입하려는 사용자가 회원가입 단계에서 노트북 화면에 입력한 비밀번호를 암호화(인코딩)하는 데 사용되는 내장 인터페이스.
      그리고, 그 입력한 비밀번호를 암호화(인코딩), 해시, 검증하는 역할임.
    - 'PasswordEncoderFactories.createDelegatingPasswordEncoder()'
      : 이 메소드가 스프링시큐리티에서 제공하는 여러 암호화 알고리즘(방식) 중 가장 적합한 것을 자동으로 선택하여 사용할 수 있게 해주는
        DelegatingPasswordEncoder 객체를 반환함.
        암호화된 비밀번호를 DB에 저장할 때 보안을 강화함
        예를 들어, {bcrypt} 접두사가 붙은 문자열이 주어지면 Bcrypt 알고리즘을,
        {scrypt} 접두사가 붙은 문자열이 주어지면 Scrypt 알고리즘을 사용하여 해당 문자열을 암호화하거나 비교합니다.
        따라서 이 코드는 다양한 암호화 방식에 대응할 수 있는 유연성과 보안성을 제공합니다.
        이런 방식은 특히 기존 시스템에서 새로운 시스템으로 이전하거나, 보안 요구 사항이 바뀌었을 때 유용합니다.
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    private void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberPrincipalService)
                .passwordEncoder(passwordEncoder());
    }


//=============================================================================================================


    private final MemberPrincipalService memberPrincipalService;
    //- 'MemberPrincipalService'
    //  : 스프링 시큐리티에서제공하는 내장 인터페이스 UserDetailsService의 구현체
    //    로그인 프로세스 중에 입력받은 사용자 이름(일반적으로 이메일 또는 아이디)에 해당하는 회원 정보를
    //    DB로부터 찾아오는 역할.
    private final RefreshTokenRepository refreshTokenRepository;
    //- 'RefreshTokenRepository'
    //   : JWT 기반 인증에서 리프레시 토큰 관련 작업(저장, 조회, 삭제 등)을 처리하기 위해 사용되는 저장소.



//=============================================================================================================

    /*

    [ 스프링 시큐리티 내장 인터페이스 AuthenticationManager 의 구현체를 @Bean 으로 등록 ]

    - 사용자가 로그인할 때 입력한 아이디, 비밀번호와 같은 사용자가 제공한 자격 증명을 검사하고 인증을 수행하는 역할.
    - OAuth 2.0과 달리, 자체 웹사이트의 자체 일반 로그인 방식에 적용되는 인증 검증 방식.
    - 동작 원리
      순서1) 사용자가 서버에 로그인 요청을 보냄
      순서2) 서버는 이 요청을 받아서 AuthenticationFilter(필터)가 처리합니다.
      순서3) 필터는 요청에서 자격 증명 정보를 추출하여 UsernamePasswordAuthenticationToken(객체)로 변환합니다.
      순서4) 필터는 이 토큰 객체를 authenticationManager() 메소드에 전달하여 인증 작업을 위임합니다.
      순서5) authenticationManager() 메소드 내부에서는 등록된 여러 개의 AuthenticationProvider(제공자) 중 하나에게 실제 인증 작업을 
            위임함.
      순서6) 각 제공자들은 자신이 처리할 수 있는 토큰 타입인지 확인하고, 맞다면 해당 제공자의 authenticate() 메소드가 호출됩니다.
      순서7) 제공자의 authenticate() 메소드 내부에서는 입력받은 자격 증명 정보와 저장되어 있는
            사용자 정보(예: 데이터베이스, LDAP 서버 등)를 비교하여 일치하는지 검사합니다.
     */


    //[ 스프링 시큐리티내장 클래스 AuthenticationConfiguration ]
    //-

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


//=============================================================================================================

    /*
    [ 스프링 시큐리티 내장 클래스 HttpSessionOAuth2AuthorizationRequestRepository 의 구현체를 @Bean 으로 등록 ]

    - 사용자가 Google, Facebook 등과 같은 다른 웹 서비스를 통해 로그인할 수 있는 기능을 제공하는 인증 방식인
      OAuth 2.0 인증 요청을 HTTP 세션에 저장하고 관리하는 클래스.
    - 동작 원리
      :
      사용자가 OAuth 2.0을 통해 로그인을 시도하면, 이 클래스는 인증 요청 정보를 HTTP 세션에 저장합니다.
      그런 다음 사용자는 새 창에서 외부 서비스로 리다이렉트되어 로그인하게 됩니다.
      로그인 후 외부 서비스는 사용자를 원래의 애플리케이션으로 리다이렉트하며,
      이때 애플리케이션이 필요로 하는 어떤 정보(예: 액세스 토큰)도 함께 전달됩니다.


     */


    @Bean
    public HttpSessionOAuth2AuthorizationRequestRepository oAuth2AuthorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }
}
