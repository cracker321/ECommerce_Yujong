package yujong.ecommerce_yujong.security.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import yujong.ecommerce_yujong.security.jwt.SecurityProvider;

import java.io.IOException;

@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private final SecurityProvider securityProvider;
    private static final String BEARER_TYPE = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        /* Header 에서 토큰 꺼내기 ->BEARER_TYPE 이 지워진 채 리턴*/
        String token = resolvedToken(request);

        /* 토큰이 공백 제외 1글자 이상이고, 유효성 검사를 통과하면 조건문 실행 */
        if (StringUtils.hasText(token) && securityProvider.validate(token)) {
            Authentication authentication = securityProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        /* 필터 체인 전달 요청 */
        filterChain.doFilter(request, response);
    }

    private String resolvedToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(BEARER_TYPE.length());
        }

        return null;
    }
}
