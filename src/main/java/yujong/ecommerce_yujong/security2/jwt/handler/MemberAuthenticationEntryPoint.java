package yujong.ecommerce_yujong.security2.jwt.handler;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import yujong.ecommerce_yujong.global.error.ErrorResponse;
import yujong.ecommerce_yujong.global.exception.ExceptionCode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/* 유효하지 않은 인증이거나 인증 정보가 부족 ->  401 Unauthorized 에러 */
@Slf4j
@Component
public class MemberAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.warn("401 Unauthorized error happened: {}", authException.getMessage());
        log.error("유효하지 않은 인증 혹은 인증 정보 없음 에러 : {}", authException.getMessage());

        sendErrorResponse(response);
    }

    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        ErrorResponse errorResponse = ErrorResponse.of(ExceptionCode.UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorResponse.getStatus());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(errorResponse, ErrorResponse.class));
    }
}