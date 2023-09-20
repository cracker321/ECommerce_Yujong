package yujong.ecommerce_yujong.security.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomAuthorityUtils {

    /*

    [ 클래스 CustomAuthorityUtils ]

    - 스프링 시큐리티에서 권한 Authority 객체를 생성하고 관리할 목적으로 만든 클래스


     */
    private final List<GrantedAuthority> CUSTOMER_ROLES = AuthorityUtils.createAuthorityList("ROLE_CUSTOMER");
    private final List<GrantedAuthority> SELLER_ROLES = AuthorityUtils.createAuthorityList("ROLE_SELLER");
    private final List<GrantedAuthority> SOCIAL_ROLES = AuthorityUtils.createAuthorityList("ROLE_SOCIAL");
    private final List<String> CUSTOMER_ROLES_STRING = List.of("CUSTOMER");
    private final List<String> SELLER_ROLES_STRING = List.of("SELLER");
    private final List<String> SOCIAL_ROLES_STRING = List.of("SOCIAL");

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