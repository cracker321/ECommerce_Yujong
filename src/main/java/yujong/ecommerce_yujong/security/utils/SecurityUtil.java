package yujong.ecommerce_yujong.security.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/*

[ SecurityUtil 클래스 ]

-

 */
@Slf4j
public class SecurityUtil {
    private SecurityUtil() {}

    public static String getCurrentEmail() {


        /*
        [ 스프링 시큐리티 내장 클래스 SeucrityContextHolder ]

        - 현재 스레드(현재 실행 중인 프래그램의 실행 흐름)의 보안 컨텍스트 정보를 저장하고 제공하는 유틸리티 클래스
        -

         */
        /* ContextHolder 에서 정보 가져오기 */
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("해당하는 인증 정보가 존재하지 않습니다.");
        }

        return authentication.getName();
    }
}