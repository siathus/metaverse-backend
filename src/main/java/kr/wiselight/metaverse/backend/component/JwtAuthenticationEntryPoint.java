package kr.wiselight.metaverse.backend.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.wiselight.metaverse.backend.constant.JwtConstant;
import kr.wiselight.metaverse.backend.controller.dto.ErrorResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("JwtAuthenticationEntryPoint Run");
        String errorMessage = (String) request.getAttribute(JwtConstant.EXCEPTION_HEADER);
        if (!StringUtils.hasText(errorMessage)) errorMessage = authException.getMessage();

        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .message(errorMessage)
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        response.getWriter()
                .write(objectMapper.writeValueAsString(responseDto));
    }
}
