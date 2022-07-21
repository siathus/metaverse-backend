package kr.wiselight.metaverse.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.*;
import kr.wiselight.metaverse.backend.component.JWTProvider;
import kr.wiselight.metaverse.backend.component.dto.UserTokenDto;
import kr.wiselight.metaverse.backend.controller.dto.*;
import kr.wiselight.metaverse.backend.controller.dto.user.UserListResponseDto;
import kr.wiselight.metaverse.backend.controller.dto.user.UserSignupDto;
import kr.wiselight.metaverse.backend.domain.token.RefreshToken;
import kr.wiselight.metaverse.backend.domain.user.User;
import kr.wiselight.metaverse.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import static kr.wiselight.metaverse.backend.constant.JwtConstant.BEARER_PREFIX;
import static kr.wiselight.metaverse.backend.constant.JwtConstant.REFRESH_TOKEN_EXPIRE_TIME;

@Slf4j
@Api(tags = {"인증 관리"}, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final UserService userService;
    private final JWTProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final RedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${wiselight.opensim.user-manipulation.server-url}")
    private String serverUrl;

    @ApiOperation(value = "로그인", notes = "로그인", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/login")
    public UserTokenDto login(@RequestBody @ApiParam(name = "Login Request Body", value = "로그인 요청 body") LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();
        log.info("login username={}", username);

        User user = userService.findByUsername(username);

        if (!passwordEncoder.matches(password, user.getPassword())) throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");

        UserTokenDto userTokenDto = tokenProvider.generateToken(user.getUsername(), user.getRole());

        // Redis에 Refresh Token 저장
        redisTemplate.opsForValue().set(user.getUsername(), RefreshToken.builder()
                .username(user.getUsername())
                .token(userTokenDto.getRefreshToken())
                .build(), REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        return userTokenDto;
    }

    @ApiOperation(value = "회원가입", notes = "회원가입", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/signup")
    public UserListResponseDto signup(@RequestBody @Validated @ApiParam(name = "Signup Request Body", value = "회원가입 요청 body") UserSignupDto dto,
                                      BindingResult bindingResult,
                                      HttpServletResponse response) throws URISyntaxException {

        if (bindingResult.hasErrors()) {
            log.info("검증 오류 발생, errors={}", bindingResult);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return null;
        }

        User savedUser = userService.save(dto);

        // OpenSimulator 서버에 유저 생성 요청
        OpenSimServerRequestDto serverRequestDto = OpenSimServerRequestDto.builder()
                .method("createuser")
                .firstName(savedUser.getUsername())
                .principalId(savedUser.getId())
                .password(savedUser.getPassword())
                .build();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.setAll(objectMapper.convertValue(serverRequestDto, new TypeReference<>() {}));

        URI uri = UriComponentsBuilder.fromUriString(serverUrl)
                .path("/accounts")
                .queryParams(params)
                .build()
                .toUri();

        try {
            ResponseEntity<OpenSimServerResponseDto> responseEntity =
                    restTemplate.postForEntity(new URI(serverUrl + "/accounts"), uri.getQuery(), OpenSimServerResponseDto.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String type = responseEntity.getBody().getResult().getType();
                if (type == null) {
                    throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "OpenSimulator server is running but creating user failed");
                }
            } else {
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "OpenSimulator server http status response is not success");
            }
        } catch (RestClientException e) {
            log.error("exception={}, message={}", e.getClass().getSimpleName(), e.getMessage());
            userService.deleteById(savedUser.getId());
            throw e;
        }
        return new UserListResponseDto(savedUser);
    }

    @ApiOperation(value = "토큰 재발급", notes = "토큰 재발급", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/refresh")
    public UserTokenDto refresh(@RequestBody @Validated @ApiParam(name = "Token Refresh Request Body", value = "토큰 재발급 요청 body") RefreshRequestDto dto) {
        String accessToken = dto.getAccessToken();
        if (accessToken.startsWith(BEARER_PREFIX)) {
            accessToken = accessToken.substring(BEARER_PREFIX.length());
        }

        // Access Token의 만료기간이 지나도 username을 읽어올 수 있도록 예외 핸들링
        String username;
        try {
            username = tokenProvider.parseClaims(accessToken).getSubject();
        } catch (ExpiredJwtException expiredJwtException) {
            username = expiredJwtException.getClaims().getSubject();
        }

        RefreshToken token = (RefreshToken) redisTemplate.opsForValue().get(username);

        User user = userService.findByUsername(token.getUsername());
        UserTokenDto userTokenDto = tokenProvider.generateToken(user.getUsername(), user.getRole());

        // Redis에 Refresh Token 갱신
        redisTemplate.opsForValue().set(user.getUsername(), RefreshToken.builder()
                .username(user.getUsername())
                .token(userTokenDto.getRefreshToken())
                .build(), REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        return userTokenDto;
    }
}
