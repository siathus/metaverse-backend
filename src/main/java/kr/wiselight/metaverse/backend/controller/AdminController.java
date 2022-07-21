package kr.wiselight.metaverse.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.wiselight.metaverse.backend.controller.dto.AdminUserUpdateRequestDto;
import kr.wiselight.metaverse.backend.controller.dto.SuccessResponseDto;
import kr.wiselight.metaverse.backend.controller.dto.message.MessageRequestDto;
import kr.wiselight.metaverse.backend.controller.dto.qna.AdminQnaResponseDto;
import kr.wiselight.metaverse.backend.controller.dto.qna.QnaReplyRequestDto;
import kr.wiselight.metaverse.backend.controller.dto.user.UserListResponseDto;
import kr.wiselight.metaverse.backend.controller.dto.user.UserResponseDto;
import kr.wiselight.metaverse.backend.domain.user.User;
import kr.wiselight.metaverse.backend.service.qna.QnaService;
import kr.wiselight.metaverse.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = {"관리자용"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final int PAGE_SIZE = 10;

    private final UserService userService;
    private final QnaService qnaService;
    private final RestTemplate restTemplate;
    private final ResourceLoader resourceLoader;

    @Value("${wiselight.opensim.broadcast.server-url}")
    private String broadcastServerUrl;

    @Value("${wiselight.opensim.broadcast.password}")
    private String broadcastPassword;

    @ApiOperation(value = "사용자 목록 조회", notes = "사용자 전체 목록 조회")
    @GetMapping("/user")
    public UserResponseDto findAll(@ApiParam("페이지 번호") @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                                   @ApiParam("검색 키워드") @RequestParam(name = "page", required = false, defaultValue = "0") int page) {
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE, Sort.Direction.ASC, "username");
        Page<User> userPage = userService.findAllByKeyword(keyword, pageRequest);

        List<UserListResponseDto> content = userPage.getContent().stream()
                .map(UserListResponseDto::new)
                .collect(Collectors.toList());

        return UserResponseDto.builder()
                .totalPages(userPage.getTotalPages())
                .totalElements(userPage.getTotalElements())
                .content(content)
                .build();
    }

    @ApiOperation(value = "사용자 조회 (아이디)", notes = "id를 통한 특정 사용자 조회")
    @GetMapping("/user/{id}")
    public UserListResponseDto findById(@PathVariable @ApiParam(name = "id", value = "사용자 ID (UUID 형식)") String id) {
        return new UserListResponseDto(userService.findById(id));
    }

    @ApiOperation(value = "사용자 수정", notes = "사용자 정보 수정")
    @PutMapping("/user/{id}")
    public SuccessResponseDto updateUser(@PathVariable @ApiParam(name = "id", value = "사용자 ID (UUID 형식)") String id,
                                         @RequestBody AdminUserUpdateRequestDto body) {
        log.info("body={}", body);
        userService.update(id, body);

        return SuccessResponseDto.builder()
                .success(true)
                .build();
    }

    @ApiOperation(value = "사용자 삭제", notes = "사용자 계정 삭제")
    @DeleteMapping("/user/{id}")
    public SuccessResponseDto deleteUserById(@PathVariable String id) {
        userService.deleteById(id);
        
        return SuccessResponseDto.builder()
                .success(true)
                .build();
    }

    @ApiOperation(value = "admin broadcast")
    @PostMapping("/message")
    public ResponseEntity<Map<String, String>> message(@RequestBody MessageRequestDto dto) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static/admin_broadcast.xml");

        String requestBody = new String(resource.getInputStream().readAllBytes());
        requestBody.replace("${password}", broadcastPassword);
        requestBody.replace("${message}", dto.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);
        headers.setConnection("close");
        HttpEntity entity = new HttpEntity(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(broadcastServerUrl, entity, String.class);
        String responseBody = responseEntity.getBody();
        log.info("response body={}", responseBody);

        if (responseBody.indexOf("error") != -1) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Opensim Server Error"));
        }
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "1:1 문의 목록 조회")
    @GetMapping("/qna")
    public AdminQnaResponseDto getQnaList(@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.Direction.DESC, "qnaId");

        return qnaService.findAll(pageable);
    }

    @ApiOperation(value = "1:1 문의 답변")
    @PostMapping("/qna")
    public SuccessResponseDto reply(@RequestBody QnaReplyRequestDto body) {
        qnaService.reply(body);

        return SuccessResponseDto.builder()
                .success(true)
                .build();
    }
}
