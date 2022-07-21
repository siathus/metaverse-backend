package kr.wiselight.metaverse.backend.service.user;

import kr.wiselight.metaverse.backend.controller.dto.AdminUserUpdateRequestDto;
import kr.wiselight.metaverse.backend.controller.dto.OpenSimSetPasswordResponseDto;
import kr.wiselight.metaverse.backend.controller.dto.user.UserListResponseDto;
import kr.wiselight.metaverse.backend.controller.dto.user.UserResponseDto;
import kr.wiselight.metaverse.backend.controller.dto.user.UserSignupDto;
import kr.wiselight.metaverse.backend.controller.dto.user.UserUpdateRequestDto;
import kr.wiselight.metaverse.backend.domain.user.User;
import kr.wiselight.metaverse.backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    @Value("${wiselight.opensim.user-manipulation.server-url}")
    private String serverUrl;

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. username = " + username));
    }

    public Page<User> findAllByKeyword(String keyword, Pageable pageable) {
        return userRepository.findAllByKeyword(keyword, pageable);
    }

    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id = " + id));
    }

    @Transactional
    public UserListResponseDto signup(UserSignupDto dto) {

        User savedUser = userRepository.save(dto.toEntity(passwordEncoder));
        

        return new UserListResponseDto(savedUser);
    }

    @Transactional
    public User save(UserSignupDto dto) {
        return userRepository.save(dto.toEntity(passwordEncoder));
    }

    @Transactional
    public void update(String id, AdminUserUpdateRequestDto dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id = " + id));
        user.update(dto);
    }

    @Transactional
    public void changePassword(String username, UserUpdateRequestDto dto) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. username = " + username));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String encodedUpdatePassword = passwordEncoder.encode(dto.getUpdatePassword());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("METHOD", "setpassword");
        params.set("PRINCIPAL", user.getId());
        params.set("PASSWORD", encodedUpdatePassword);

        String query = UriComponentsBuilder.fromHttpUrl(serverUrl)
                .queryParams(params)
                .build()
                .toUri()
                .getQuery();

        ResponseEntity<OpenSimSetPasswordResponseDto> responseEntity =
                restTemplate.postForEntity(serverUrl + "/auth/plain", query, OpenSimSetPasswordResponseDto.class);

        OpenSimSetPasswordResponseDto responseBody = responseEntity.getBody();
        log.info("Response Result={}", responseBody.getResult());

        if (responseBody.getResult().equals("Failure")) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "OpenSimulator server is running but change password failed");
        }

        user.setPassword(encodedUpdatePassword);
    }

    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Transactional
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }
}
