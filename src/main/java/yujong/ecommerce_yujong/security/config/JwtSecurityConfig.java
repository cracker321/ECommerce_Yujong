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
    [ SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>{} ]

    - 스프링 시큐리티의 기본 설정을 확장하고 사용자 정의 설정을 적용하는 클래스.
    - 비유&실생활 예시
      * 'SecurityConfigurerAdpater<>{}': 집주인의 입장이 되어, 집 대문의 잠금 장치를 어떠헥 설정할지,
                                         누구에게 열어 줄지 결정하는 역할.
                                         DefaultSecurityFilterChain 및 HttpSecurity 와 조합하여
                                         보안 설정을 구축하고, 사용자 지정 보안 로직을 추가하는 역할을 함.
      * 'DefaultSecurityFilterChain': 집 대문에 설정된 잠금장치 자체를 의미. 스프링 시큐리티가 기본 제공하는 디폴트 필터 체인임.
      * 'HttpSecurity': 이 잠금장치를 만들 때 필요한 재료, 도구. 즉, SecurityConfig 파일에서 작성한
                        시큐리티 필터 체인.
                        특정 Http 요청에 적용할 보안 설정을 구성하는 데 사용되는 것임.
                        이를 통해, 어떤 사람(요청)이 들어올 수 있고, 어떤 사람이 들어올 수 있는지를 설정하는 것임.
      

     */
    private final SecurityProvider securityProvider;



    /*
    [ 오버라이딩한 메소드 configure(HttpSecurity http){} ]

    - 'new SecurityFilter(securityProvider)'
      : 사용자 정의(커스터마이징한) 새로운 보안 기능(시큐리티 필터)을 추가하는 메소드.
        스프링 시큐리티 내장 클래스 SecurityFilter().
        즉, SecurityFilter 객체 내부에 이미 사용자 정의 필터 코드를 내가 작성해놨고,
        그것을 여기에 추가로 적용시키는 것임.
        사용자 정의 필터 로직을 추가하여 보안 로직을 좀 더 세밀하게 만들 수 있음.
    - 'http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)
      * 'http': 스프링 시큐리티에서 보안 설정을 담당하는 내장 객체 HttpSecurity
      * 'addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)'
        : 스프링 시큐리티 내장 클래스 HttpSecurity 의 내장 메소드 addFilterBefore.
          사용자 정의 보안 필터 customFilter를 인자로 들어온 UsernamePasswordAuthenticationFilter 클래스 보다
          앞에 배치하여, 이 클래스 실행 전에 먼저 우선하여 동작하도록 설정함.
          즉, 필터 체인 중에서 UsernamePasswordAuthenticationFilter 체인보다 customFilter 체인이 먼저 실행되는 것임.
          addFilterBefore 메소드를 사용하면, 특정 필터를 다른 필터보다 우선하여 작동시키도록 순서를 지정할 수 있음.
      * 'UsernamePasswordAuthenticationFilter'
        : 스프링 시큐리티에서 제공하는 필터로, Http 요청에서 사용자 이름과 패스워드를 추출하여 인증을 수행함.
          HTTP 요청의 특정 파라미터(username, password 기본값)에서 정보를 추출함.
          POST 요청과 연동되며, 보통 /login 경로에서 작동함.
          실패하면 설정된 AuthenticationFailureHandler를, 성공하면 AuthenticationSuccessHandler를 호출함.

     */
    @Override //메소드 오버라이딩
    public void configure(HttpSecurity http) {
        SecurityFilter customFilter = new SecurityFilter(securityProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }

}   
