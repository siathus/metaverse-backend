package kr.wiselight.metaverse.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.wiselight.metaverse.backend.controller.dto.SuccessResponseDto;
import kr.wiselight.metaverse.backend.controller.dto.qna.QnaSaveRequestDto;
import kr.wiselight.metaverse.backend.controller.dto.qna.UserQnaResponseDto;
import kr.wiselight.metaverse.backend.controller.dto.user.UserCheckRequestDto;
import kr.wiselight.metaverse.backend.controller.dto.user.UserListResponseDto;
import kr.wiselight.metaverse.backend.controller.dto.user.UserUpdateRequestDto;
import kr.wiselight.metaverse.backend.domain.user.User;
import kr.wiselight.metaverse.backend.service.qna.QnaService;
import kr.wiselight.metaverse.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Api(tags = {"user"}, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private static final int PAGE_SIZE = 10;

    private final UserService userService;
    private final QnaService qnaService;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "마이페이지", notes = "현재 로그인 중인 유저의 정보 표시")
    @GetMapping("")
    public UserListResponseDto myPage(@ApiIgnore Authentication authentication) {
        String username = authentication.getName();

        User user = userService.findByUsername(username);

        return new UserListResponseDto(user);
    }

    @ApiOperation(value = "사용자 정보 수정", notes = "사용자 정보 수정")
    @PutMapping("")
    public SuccessResponseDto update(@RequestBody @ApiParam(name = "비밀번호 변경 body", value = "비밀번호 변경") UserUpdateRequestDto body,
                                     @ApiIgnore Authentication authentication) {
        String username = authentication.getName();

        userService.changePassword(username, body);

        return SuccessResponseDto.builder()
                .success(true)
                .build();
    }

    @ApiOperation(value = "회원탈퇴", notes = "회원탈퇴")
    @PostMapping("/withdrawal")
    public SuccessResponseDto withdrawal(@RequestBody @ApiParam(name = "password", value = "비밀번호") UserCheckRequestDto dto,
                                         @ApiIgnore Authentication authentication) {
        String username = authentication.getName();
        String requestPassword = dto.getPassword();

        User user = userService.findByUsername(username);

        if (!passwordEncoder.matches(requestPassword, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 1:1 문의 삭제
        qnaService.deleteByUserId(user.getId());

        // 회원 삭제
        userService.delete(user);
        return SuccessResponseDto.builder()
                .success(true)
                .build();
    }

    @ApiOperation(value = "작성한 1:1 문의 내역")
    @GetMapping("/qna")
    public UserQnaResponseDto getQnaList(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                         @ApiIgnore Authentication authentication) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.Direction.DESC, "qnaId");
        String username = authentication.getName();

        User user = userService.findByUsername(username);

        return qnaService.findByUserId(user, pageable);
    }

    @ApiOperation(value = "1:1 문의 작성")
    @PostMapping("/qna")
    public SuccessResponseDto saveQna(@RequestBody QnaSaveRequestDto body,
                                      @ApiIgnore Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        qnaService.save(body, user);

        return SuccessResponseDto.builder()
                .success(true)
                .build();
    }
}
