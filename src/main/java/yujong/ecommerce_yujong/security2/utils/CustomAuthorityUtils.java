package yujong.ecommerce_yujong.security2.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component //이제 아래 클래스는 스프링 IoC 컨테이너가 관리하는 클래스가 되는 것이다.
public class CustomAuthorityUtils {

    /*

    [ 클래스 CustomAuthorityUtils ]

    - 스프링 시큐리티에서 권한 Authority 객체를 생성하고 관리할 목적으로 만든 클래스


    < 필드: private final ~ >
    - 이 클래스는 미리 정의된 세 가지 권한 CUSTOMER_ROLES, SELLER_ROLES_SOCIAL_ROLES 를 나타내는
      GranedAuthority 객체와 문자열 리스트 List를 '필드'로 가지고 있음.
    - GrantedAuthority 는 스프링 시큐리티에서 권한을 나타내는 인터페이스임.
    - 이 필드들은 각각의 역할 대해 GrantedAuthority 객체와 문자열 리스트들 빠르게 반환받기 위해 사용되는 것임.


     */
    private final List<GrantedAuthority> CUSTOMER_ROLES = AuthorityUtils.createAuthorityList("ROLE_CUSTOMER");
    private final List<GrantedAuthority> SELLER_ROLES = AuthorityUtils.createAuthorityList("ROLE_SELLER");
    private final List<GrantedAuthority> SOCIAL_ROLES = AuthorityUtils.createAuthorityList("ROLE_SOCIAL");
    private final List<String> CUSTOMER_ROLES_STRING = List.of("CUSTOMER");
    private final List<String> SELLER_ROLES_STRING = List.of("SELLER");
    private final List<String> SOCIAL_ROLES_STRING = List.of("SOCIAL");

    
    /*

    [ 메소드 createAuthorities ]

    - 위의 필드들에서 입력받은 문자열 리스트 roles 들로부터 GrantedAuthority 객체의 리스트를 생성하고 반환함.

     */
    public List<GrantedAuthority> createAuthorities(String role) {
        if (role.equalsIgnoreCase("CUSTOMER")) {
            return CUSTOMER_ROLES;
        } else if (role.equalsIgnoreCase("SELLER")) {
            return SELLER_ROLES;
        } else if (role.equalsIgnoreCase("SOCIAL")) {
            return SOCIAL_ROLES;
        } else throw new RuntimeException("잘못된 접근입니다.");
    }

    
    
    public List<GrantedAuthority> createAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        return authorities;
    }



    public List<String> createRoles(String roles) {
        if (roles.equalsIgnoreCase("CUSTOMER")) {
            return CUSTOMER_ROLES_STRING;
        } else if (roles.equalsIgnoreCase("SELLER")) {
            return SELLER_ROLES_STRING;
        } else if (roles.equalsIgnoreCase("SOCIAL")) {
            return SOCIAL_ROLES_STRING;
        } else throw new RuntimeException("잘못된 접근입니다.");
    }
}