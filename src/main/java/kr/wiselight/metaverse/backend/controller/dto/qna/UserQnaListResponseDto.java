package kr.wiselight.metaverse.backend.controller.dto.qna;

import kr.wiselight.metaverse.backend.domain.user.Qna;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class UserQnaListResponseDto {

    private Long qnaId;
    private String title;
    private String content;
    private String answer;
    private LocalDateTime createdDate;

    public UserQnaListResponseDto(Qna qna) {
        this.qnaId = qna.getQnaId();
        this.title = qna.getTitle();
        this.content = qna.getContent();
        this.answer = qna.getAnswer();
        this.createdDate = qna.getCreatedDate();
    }
}
