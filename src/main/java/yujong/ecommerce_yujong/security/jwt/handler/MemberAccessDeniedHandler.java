package yujong.ecommerce_yujong.security.jwt.handler;

import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/* 접근에 필요한 권한 없이 접근 -> 403 forbidden 에러 */
@Slf4j
@Component
public class MemberAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("403 Forbidden error happened: {}", accessDeniedException.getMessage());
        log.error("접근 권한 없음 에러 : {}", accessDeniedException.getMessage());
        sendErrorResponse(response);
    }

    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        ErrorResponse errorResponse = ErrorResponse.of(ExceptionCode.FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorResponse.getStatus());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(errorResponse, ErrorResponse.class));
    }