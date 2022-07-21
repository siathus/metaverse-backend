package kr.wiselight.metaverse.backend.controller.dto.notice;

import kr.wiselight.metaverse.backend.domain.notice.Notice;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class NoticeDetailResponseDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public NoticeDetailResponseDto(Notice notice) {
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.createdDate = notice.getCreatedDate();
        this.lastModifiedDate = notice.getLastModifiedDate();
    }
}
