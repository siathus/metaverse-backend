package kr.wiselight.metaverse.backend.controller;

import io.swagger.annotations.*;
import kr.wiselight.metaverse.backend.controller.dto.SuccessResponseDto;
import kr.wiselight.metaverse.backend.controller.dto.notice.NoticeListResponseDto;
import kr.wiselight.metaverse.backend.controller.dto.notice.NoticeRequestDto;
import kr.wiselight.metaverse.backend.controller.dto.notice.NoticeDetailResponseDto;
import kr.wiselight.metaverse.backend.controller.dto.notice.NoticeResponseDto;
import kr.wiselight.metaverse.backend.domain.notice.Notice;
import kr.wiselight.metaverse.backend.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"notice"})
@Slf4j
@RequiredArgsConstructor
@RestController
public class NoticeController {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final int PAGE_SIZE = 10;

    private final NoticeService noticeService;

    @ApiOperation(value = "공지사항 목록 조회", notes = "공지사항의 전체 목록 조회")
    @GetMapping("/notice")
    public NoticeResponseDto findAll(@ApiParam(value = "페이지 번호") @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                     @ApiParam(value = "검색 키워드") @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword) {
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE, Sort.Direction.DESC, "id");
        Page<Notice> noticePage = noticeService.findAll(keyword, pageRequest);

        List<NoticeListResponseDto> list = noticePage.getContent().stream()
                .map(NoticeListResponseDto::new)
                .collect(Collectors.toList());

        return NoticeResponseDto.builder()
                .content(list)
                .totalPages(noticePage.getTotalPages())
                .totalElements(noticePage.getTotalElements())
                .build();
    }

    @ApiOperation(value = "공지사항 내용 조회")
    @GetMapping("/notice/{id}")
    public NoticeDetailResponseDto findById(@ApiParam(value = "공지사항 번호") @PathVariable Long id) {
        return noticeService.findById(id);
    }

    @ApiOperation(value = "공지사항 저장")
    @Secured(ROLE_ADMIN)
    @PostMapping("/notice")
    public SuccessResponseDto save(@RequestBody NoticeRequestDto body) {

        noticeService.save(body);

        return SuccessResponseDto.builder()
                .success(true)
                .build();
    }

    @ApiOperation(value = "공지사항 수정")
    @Secured(ROLE_ADMIN)
    @PutMapping("/notice/{id}")
    public SuccessResponseDto update(@ApiParam(value = "공지사항 번호") @PathVariable Long id,
                                     @RequestBody NoticeRequestDto body) {
        noticeService.update(id, body);

        return SuccessResponseDto.builder()
                .success(true)
                .build();
    }

    @ApiOperation(value = "공지사항 삭제")
    @Secured(ROLE_ADMIN)
    @DeleteMapping("/notice/{id}")
    public SuccessResponseDto delete(@ApiParam(value = "공지사항 번호") @PathVariable Long id) {
        noticeService.delete(id);

        return SuccessResponseDto.builder()
                .success(true)
                .build();
    }
}
