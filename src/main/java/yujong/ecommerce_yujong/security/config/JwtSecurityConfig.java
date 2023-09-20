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



     */
    @Override //메소드 오버라이딩
    public void configure(HttpSecurity http) {
        SecurityFilter customFilter = new SecurityFilter(securityProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }

}   
