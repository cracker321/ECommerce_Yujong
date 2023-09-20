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
        - 'SecurityContextHolder.getContext()':

        - getContext().getAuthentication().getName(): 현재 로그인 완료되어 인증된 사용자의 이름
        - getContext().getAuthentication().getPrincipal(): 현재 로그인 완료되어 인증된 사용자 객체 UserDetails 객체 반환함.
                                                           사용자의 상세 정보와 권한 정보가 들어 있음.
        - getContext().getAuthentication().getCredentials(): 현재 로그인 완료되어 인증된 사용자의 비밀번호 또는 토큰
        - getContext().getAuthentication().getAuthorities(): 현재 로그인 완료되어 인증된 사용자의 권한 목록.
                                                             각 권한은 GrantedAuthority 객체로 표현됨
        - getContext().getAuthentication().isAuthenticated(): 현재 로그인 완료되어 인증된 사용자가 '현재 인증되었는지 여부'를
                                                              boolean 타입으로 반환함.
        - getContext().getAuthentication().getDetails(): 현재 로그인 완료되어 인증된 사용자의 추가 정보를 객체로 반환함.
                                                         사용자의 IP 주소 또는 세션 정보와 같은 추가 정보가 들어있을 수 있음.
          순서1)
          사용자가 로그인하면, 스프링 시큐리티는 현재 스레드의 SecurityContext에 해당 사용자의 보안 정보를 설정합니다.
          이 정보에는 사용자의 식별 정보(예: 사용자 이름) 및 권한(예: 어떤 작업을 할 수 있는지)가 포함됩니다.
          이제 이 스레드에서는 언제든지 현재 사용자의 정보에 접근할 수 있습니다.


         */
        /* ContextHolder 에서 정보 가져오기 */

        //< 현재 로그인 완료되어 인증된 사용자의 정보 전체 전반을 SecurityContextHolder를 통해 가져옴 >
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("해당하는 인증 정보가 존재하지 않습니다.");
        }

        return authentication.getName();
    }
}