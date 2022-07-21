package kr.wiselight.metaverse.backend.component;

import io.jsonwebtoken.*;
import kr.wiselight.metaverse.backend.component.dto.UserTokenDto;
import kr.wiselight.metaverse.backend.domain.user.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static kr.wiselight.metaverse.backend.constant.JwtConstant.ACCESS_TOKEN_EXPIRE_TIME;
import static kr.wiselight.metaverse.backend.constant.JwtConstant.REFRESH_TOKEN_EXPIRE_TIME;

@Slf4j
@RequiredArgsConstructor
@Component
public class JWTProvider {

    private static final String AUTHORITIES_KEY = "authorities";
    private static final String BEARER_TYPE = "Bearer ";
//    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분

    private final UserDetailsService userDetailsService;

    @Value("${wiselight.security.jwt.secret}")
    private String secretKey;

    public UserTokenDto generateToken(String username, Role authority) {
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .claim(AUTHORITIES_KEY, authority)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return UserTokenDto.builder()
                .accessToken(BEARER_TYPE + accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
//        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return true;
//        }
//        catch (SignatureException ex) {
//            log.error("Invalid JWT signature");
//        } catch (MalformedJwtException ex) {
//            log.error("Invalid JWT token");
//        } catch (ExpiredJwtException ex) {
//            log.error("ExpiredJwtException");
//        } catch (UnsupportedJwtException ex) {
//            log.error("Unsupported JWT token");
//        } catch (IllegalArgumentException ex) {
//            log.error("JWT claims string is empty.");
//        }
    }
}
