package yujong.ecommerce_yujong.security2.utils;


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

    //static으로 선언되었기에, 외부 클래스 어디에서나 getCurrentEmail() 메소드를
    //따로 객체 생성하지 않고 바로 호출해서 사용 가능함.
    public static String getCurrentEmail() {


        /*
        [ 스프링 시큐리티 내장 클래스 SeucrityContextHolder ]

        - 스프링 시큐리티 내장 객체 SecurityContextHolder는 현재 스레드(현재 실행 중인 프래그램의 실행 흐름)의
          보안 컨텍스트 정보를 저장하고 제공하는 유틸리티 클래스임.
          SecurityContextHolder 를 통해 현재 실행 중인 스레드와 연관된 보안 컨텍스트에 접근할 수 있음.
          SecurityContextHolder 안에는 Authentication 객체가 들어있음.
        - 스프링 시큐리티 내장 객체 Authentication 는 스프링 시큐리티에서 로그인이 완료되어 인증된 현재 사용자의 인증 정보가
          저장되는 객체임.
          이 객체는 주로 사용자의 ID, 비밀번호, 권한 정보 등을 포함함.
          보통 AuthenticationProvider 에 의해 생성되며, 그 후 SecurityContext 에 저장됨.
          이렇게 함으로써, 애플리케이션의 다른 부분에서도 현재 인증된 사용자의 정보를 쉽게 참조할 수 있음.
        - 'SecurityContextHolder.getContext().getAuthentication()


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

        //순서1) 현재 로그인 완료되어 인증된 사용자의 정보 전체 전반을 SecurityContextHolder를 통해 가져옴 >
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        //순서2) 만약 사용자의 인증정보를 담고 있는 Authentication 객체가 null(아직 로그인 인증 완료된 사용자 없음) 또는
        //      authentication.getName() 값이 null(로그인 인증 완료된 사용자의 이름/이메일 정보가 없음)인 경우에는
        //      RuntimeException을 발생시킴.
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("해당하는 인증 정보가 존재하지 않습니다.");
        }

        //순서3) 사용자의 이름(또는 이메일)을 반환해줌.
        return authentication.getName();
    }
}



 /*

    < 스프링 시큐리티 '세션' >

    - 사용자의 인증 정보를 세션에 저장하여 관리함.
    - 세션에 저장되는 정보는 'Authentication 객체' 딱 하나임.
    - 사용자가 로그인하면, 스프링 시큐리티는 Authentication 객체를 생성하고, 인증된 사용자의 정보를 담아 세션에 저장함.
                 ㅣ
                 ㅣ
                 ㅣ 하위 개념으로 내려가는 것.
                 ㅣ
                 ㅣ
    < 내장 Authentication 객체 >

    - 스프링 시큐리티에서 '사용자의 신원을 확인하는 과정'에서 사용되며, '로그인(인증)에 성공한 사용자'를 나타내는 객체임.
      사용자가 누구인지 확인하고, 애플리케이션에 대한 접근 '권한'이 있는지 확인하는 절차임.
      일반적으로, 사용자는 ID, 비밀번호, 이메일 또는 소셜 계정 등을 통해 인증됨.
    - '내장 Authentication 객체'는, '내장 Principal 객체'와 '내장 Credentials 객체' 두 가지 정보를 포함하여 구성됨.
    - '내장 Principal 객체'
       :'인증된 사용자(=사용자의 인증 정보)를 나타내는 객체'로,
        주로 사용자의 신원정보(=식별자)(이름 username, 이메일 email 등)를 포함하고 있음.
    - '내장 Credentials 객체'
       : '사용자가 제공한 자격 증명(비밀번호, 토큰 등)'을 나타내는 객체임.
                 ㅣ
                 ㅣ
                 ㅣ 하위 개념으로 내려가는 것.
                 ㅣ
                 ㅣ
    < 내장 Authentication 객체의 두 개의 제한된 필드: '내장 인터페이스 UserDetails 타입'과 '내장 인터페이스 OAuth2User 타입' >

    - 내장 Authentication 객체에는 특정한 필드만 저장할 수 있음.
      따라서, 원칙적으로는 내장 Authentification 객체는
      (1)'일반 로그인 스프링 시큐리티 인증 정보' 또는 (2)'소셜 로그인 OAuth2 인증 정보'
      중 오직 '단 한 가지만 저장'할 수 있음.
    - 1. '내장 인터페이스 UserDetails'
         : '일반 로그인'할 때 사용되는 스프링 시큐리티에서 제공하는 내장 인터페이스.
           '사용자 정보'와 '권한 정보'를 담고 있음
    - 2. '내장 인터페이스 OAuth2User'
         : 'OAuth2 인증 로그인(=소셜 로그인)' 방식을 사용할 때 사용되는 내장 인터페이스로, '사용자 '속성' 정보'를 담고 있음.
                 ㅣ
                 ㅣ
                 ㅣ 하위 개념으로 내려가는 것.
                 ㅣ
                 ㅣ
    < '내장 인터페이스 UserDetails'와 '내장 인터페이스 OAuth2User'에 '사용자의 세부 정보'를 전달해주기 위해,
     각각 'DB'와 'OAuth 2.0 제공자(=구글)'로부터 그 '사용자의 세부 정보'를 '조회해서 가져오는 역할'을 하는
      '내장 인터페이스 서비스 UserDetailsService'와 '내장 인터페이스 서비스 DeafaultOAuth2User' >

    1-(1). '내장 인터페이스 UserDetails'
           - '사용자의 세부 정보를 담고 있는 클래스 PrincipalDetails'가 구현하고 있는 내장 인터페이스로,
             사용자의 계정 정보와 권한 정보를 담고 있는 인터페이스임.
             인증 과정에서 '이 내장 인터페이스를 구현하는 클래스 PrincipalDetails'를 사용하여 사용자의 세부 정보를 제공함.
           - 주요 내장 메소드:
             getUsername(): 사용자의 이름을 반환함.
             getPassowrd(): 사용자의 비밀번호를 반환함.
             getAuthorities(): 사용자가 가지고 있는 권한 목록을 반환함.
             isAccountNonExpired(): 사용자의 계정이 만료되었는지 여부를 반환함.
             isAccountNonLocked(): 사용자의 계정이 잠겨있는지 여부를 반홤함.
             isCredentialsNonExpired(): 사용자의 인증 정보가 만료되었는지 여부를 반환함.
             isEnabled(): 사용자의 계정이 활성화되었는지 여부를 반환함.

    1-(2). '내장 인터페이스 서비스 UserDetailsService'
           - 'DB로부터' '사용자의 세부 정보'를 '조회해서 가져오는 역할'을 담당함.
           - 주요 내장 메소드:
             loadUserByUsername(String username) : 주어진 사용자 이름에 해당하는 사용자 정보를 'DB로부터' 조회하여
                                                   'UserDetails 객체를 반환'한다!



    2-(1). '내장 인터페이스 OAuth2User'
           - '사용자의 세부 정보를 담고 있는 클래스 PrincipalDetails'가 구현하고 있는 내장 인터페이스로,
             OAuth 2.0 기반의 인증을 통해 얻은 사용자의 '속성(attributes)' 정보를 담고 있는 인터페이스임.
             인증 과정에서 '이 내장 인터페이스를 구현하는 클래스 PrincipalDetails'를 사용하여 사용자의 속성 정보를 제공함.
           - 주요 내장 메소드:
             getName(): OAuth 2.0으로 인증된 사용자의 이름을 반환함. 사용자를 식별하는 데에 사용될 수 잇음.
             getAttributes(): 인증된 '사용자의 속성(attribute) 정보'를 반환함.
                              이는 보통 사용자의 프로필 정보와 같은 추가 정보를 제공하는 데 활용됨.
             getAuthorities(): 인증된 '사용자의 권한 정보'를 반환함.
                               이는 보통 권한 부여 및 인가 작업에 사용됨.


    2-(2). '내장 인터페이스 서비스 DefaultOAuth2UserService'
           - 'OAuth2 제공자(=구글 등)로부터' '사용자의 세부 정보'를 '조회해서 가져오는 역할'을 담당함.
           - 주요 내장 메소드:
             loadUser(OAuth2UserRequest userRequest): 주어진 사용자 이름에 해당하는 사용자 정보를
                                                     'OAuth 2.0 제공자(=구글 등)로부터' 조회하여 'OAuth2User 객체를 반환'함!
                 ㅣ
                 ㅣ
                 ㅣ 하위 개념으로 내려가는 것.
                 ㅣ
                 ㅣ
    < '사용자의 세부 정보'를 담고 있는 '내장 인터페이스 UserDetails'와 '내장 인터페이스 OAuth2User'를
      구현하기 위해 만든 '사용자 정의 클래스 PrincipalDetails' >

    - '개발자가 필요로 하는 사용자 User의 상세 정보'를 담을 수 있도록 만든 클래스이며, 'User 객체'를 '포함'하여 사용자 정보를 저장함.
    - '내장 인터페이스 UserDetails' 또는 '내장 인터페이스 OAuth2SUer'를 '커스터마이징'하여 구현(implements)'하기 때문에,
      이 내장 인터페이스들 내부의 '사용자 정보와 권한을 제공하는 내장 메소드들'을 '오버라이딩'하여 '정의'해야 함(인터페이스이기 때문).
      또한, '커스터마이징한 클래스'이기 때문에, '사용자 User 정보'에 추가적인 필드들을 포함시켜도 됨.
    - 사용자 정보를 활용한 권한 검사, 추가 필드 활용, 특정 조건에 따른 동작 변경 등의 기능을 구현할 때 호라용됨.
    - '내장 객체 Authentication'은, 인증된 사용자를 식별하기 위해 'PrincipalDetails 객체'를 '스프링 시큐리티 세션에 저장'하고,
      이렇게 '세션에 저장된 Authentication 객체'는 후속 요청에서 사용자를 식별하고, 인증된 사용자의 정보에 접근하는 데 사용됨.

     */

//============================================================================================

//[ '스프링부트 시큐리티 7강 - 구글 회원 프로필 정보 받아보기'강 06:40~ ]


//< 구글 로그인 인증(=OAuth2 인증)이 완료된 뒤, 그 후 절차임 >
// &
//< 스프링 시큐리티 내장 클래스 DefaultOAuth2UserService의 내장 메소드 loadUser >


// 순서1) '내장 메소드 loadUser'
// - '스프링 시큐리티 내장 클래스 DefaultOAuth2UserService의 오버라이딩 내장 메소드 loadUser'는,
//   구글 로그인이 완료된 이후에 호출되며,
//   구글 로그인 후 구글API로부터 전달받은(반환된) 'OAuth2UserRequest 객체'를 인자값으로 하여
//   이 'OAuth2UserRequest 객체'를 매개변수로 받음.
//   이 'OAuth2UserRequest 객체'는, OAuth2 인증을 위해 필요한 정보를 포함하고 있음.


// 순서2) 'OAuth2UserRequest 객체'
// - 스프링 시큐리티의 OAuth2 로그인(=구글 로그인)을 수행한 뒤 호출되는 메소드로, 구글 로그인이 완료되면,
//   개발자는 구글API로부터 'OAuth2UserRequest 객체'를 반환받는데,
//   이를 인자값으로 받아 사용하여, 구글 API로부터 사용자 프로필 정보를 가져옴.
// - 'OAuth2UserRequest 객체'는, '로그인 인증 완료된 사용자의 프로필 정보'와 '엑세스 토큰'을 요청하는 데 필요한 정보인
//   'ClientRegistration 객체'와 'AuthorizedClient 객체'를 담고 있음.


// 순서2)-1 'OAuth2UserRequest 객체'로부터 'ClientRegistration 객체'를 가져온다.
// - 'ClientRegistration 객체'는 구글(=OAuth2 공급자)과 사용자(e.g: yujong4170@gamil.com) 간의 등록 정보를 나타냄.


// 순서2)-2 'ClientRegistration 객체'를 사용하여, 구글(=OAuth2 공급자)에게 '엑세스 토큰 AccessToken'을 요청하여 발급받아온다.
// - '엑세스 토큰'은 구글로부터 발급되는 인증 토큰임. 이 토큰은, (로그인)인증된 사용자를 식별하고 그 사용자에게 권한을 부여하는 데
//   사용됨.ㄷ


// 순서2)-3 '순서 2)-2'에서 구글로부터 발급받은 '엑세스 토큰'을 사용하여, 이제 구글에게 그 (로그인)인증된 사용자 정보를 요청한다.
// - 이를 통해, 구글로부터 (로그인)인증된 사용자의 프로필 정보를 가져옴. 바로 여기 단계에서 구글과의 통신이 이루어짐.


// 순서3) 구글로부터 받아온 '사용자 프로필 정보'를 기반으로 '내장 OAuth2User 객체'를 생성함.
// - 이 '(로그인)인증된 사용자 정보를 담고 있는 내장 OAuth2User 객체'는 '인터페이스'임.
//   사용자의 식별자(id값), 이름, 이메일 등의 속성을 포함하고 있음.


// 순서4) '생성된 사용자 프로필 정보 OAuth2User 객체를 반환'함: return super.loadUser(userRequest)
// - '내장 클래스 DefaultOAuth2UserService'의 '내장 메소드 loadUser'를 호출하여, 기본 구현을 활용함.
//   '기본 구현'의 역할은 '구글로부터 사용자 프로필 정보를 가져오고',
//   'OAuth2User 객체를 생성'하여, 그 'OAuth2User 객체에 그 사용자의 프로필 정보를 담아'서 '반환 return'해주는 것임.