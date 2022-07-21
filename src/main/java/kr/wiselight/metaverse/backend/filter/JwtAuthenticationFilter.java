package kr.wiselight.metaverse.backend.filter;

import kr.wiselight.metaverse.backend.component.JWTProvider;
import kr.wiselight.metaverse.backend.constant.JwtConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static kr.wiselight.metaverse.backend.constant.JwtConstant.BEARER_PREFIX;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("request URI={}", request.getRequestURI());
        String token = getTokenFromRequest(request);
        try {
            if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("Exception={}, message={}", e.getClass().getTypeName(), e.getMessage());
            request.setAttribute(JwtConstant.EXCEPTION_HEADER, e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String requestToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasLength(requestToken) && requestToken.startsWith(BEARER_PREFIX)) {
            return requestToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}
