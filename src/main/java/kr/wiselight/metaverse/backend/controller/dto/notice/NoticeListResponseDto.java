package kr.wiselight.metaverse.backend.controller.dto.notice;

import kr.wiselight.metaverse.backend.domain.notice.Notice;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class NoticeListResponseDto {

    private Long id;
    private String title;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public NoticeListResponseDto(Notice notice) {
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.createdDate = notice.getCreatedDate();
        this.lastModifiedDate = notice.getLastModifiedDate();
    }
}
