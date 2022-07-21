package kr.wiselight.metaverse.backend.controller.dto.notice;

import kr.wiselight.metaverse.backend.domain.notice.Notice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NoticeRequestDto {

    private String title;
    private String content;

    public Notice toEntity() {
        return Notice.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }
}
