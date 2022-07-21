package kr.wiselight.metaverse.backend.domain.notice;

import kr.wiselight.metaverse.backend.controller.dto.notice.NoticeRequestDto;
import kr.wiselight.metaverse.backend.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Notice extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Builder
    public Notice(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void update(NoticeRequestDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
    }
}
