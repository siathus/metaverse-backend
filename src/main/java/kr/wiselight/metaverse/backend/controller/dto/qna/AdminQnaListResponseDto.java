package kr.wiselight.metaverse.backend.controller.dto.qna;

import kr.wiselight.metaverse.backend.domain.user.Qna;
import kr.wiselight.metaverse.backend.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AdminQnaListResponseDto {

    private Long qnaId;
    private LocalDateTime createdDate;
    private String username;
    private String title;
    private String content;
    private String answer;

    public AdminQnaListResponseDto(Qna qna) {
        this.qnaId = qna.getQnaId();
        this.createdDate = qna.getCreatedDate();
        this.title = qna.getTitle();
        this.content = qna.getContent();
        this.answer = qna.getAnswer();
        this.username = qna.getUsername();
    }
}
