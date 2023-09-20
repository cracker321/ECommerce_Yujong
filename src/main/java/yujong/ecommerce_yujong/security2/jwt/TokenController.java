package yujong.ecommerce_yujong.security2.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import yujong.ecommerce_yujong.global.exception.BusinessLogicException;
import yujong.ecommerce_yujong.global.exception.ExceptionCode;
import yujong.ecommerce_yujong.member.entity.Member;
import yujong.ecommerce_yujong.member.mapper.MemberMapper;
import yujong.ecommerce_yujong.member.service.MemberService;
import yujong.ecommerce_yujong.security2.jwt.dto.LoginResponse;
import yujong.ecommerce_yujong.security2.jwt.dto.TokenDto;
import yujong.ecommerce_yujong.security2.service.SecurityService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final SecurityService securityService;
    private final MemberService memberService;
    private final MemberMapper mapper;



    /* 엑세스 토큰 재발급 */
    @PostMapping("/reissue/access")
    public ResponseEntity newAccessToken(@RequestHeader("Authorization") String accessToken) {
        String responseToken = securityService.reissueAccess(accessToken);

        HttpHeaders httpHeaders = setHeader(responseToken);
        String message = "액세스 토큰 재발급 완료.";

        return new ResponseEntity<>(message, httpHeaders, HttpStatus.OK);
    }



    /* 리프레시 토큰 + 엑세스 토큰 재발급 */
    @PostMapping("/reissue/refresh")
    public ResponseEntity newRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        TokenDto tokenDto = securityService.reissueRefresh(request, response);

        HttpHeaders httpHeaders = setHeader(tokenDto.getAccessToken());
        String message = "토큰 재발급 완료.";

        return new ResponseEntity<>(message, httpHeaders, HttpStatus.OK);
    }




    /* 새로 고침 */
    @GetMapping("/access")
    // @ReissueToken /* 토큰 재발급 안함 */
    public ResponseEntity reGet(HttpServletRequest request, Principal principal) {
        Member member = memberService.findMemberByEmail(principal.getName());
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");
        HttpHeaders headers = setHeader(accessToken);

        if(member.getRole().equals("SELLER")) {
            LoginResponse.Seller sellerResponse = mapper.getSellerResponse(member);

            return new ResponseEntity<>(sellerResponse, headers, HttpStatus.OK);
        } else if (member.getRole().equals("CLIENT")) {
            LoginResponse.Customer customerResponse = mapper.getCustomerResponse(member);

            return new ResponseEntity<>(customerResponse, headers, HttpStatus.OK);
        }

        throw new BusinessLogicException(ExceptionCode.WRONG_ACCESS);
    }

    /* 로그인 헤더 설정 */
    private HttpHeaders setHeader(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", token);

        return httpHeaders;
    }
}