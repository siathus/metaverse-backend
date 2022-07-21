package kr.wiselight.metaverse.backend.controller.dto.notice;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class NoticeResponseDto {

    private int totalPages;
    private long totalElements;
    private List<NoticeListResponseDto> content;

    @Builder
    public NoticeResponseDto(int totalPages, long totalElements, List<NoticeListResponseDto> content) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.content = content;
    }
}
