package kr.wiselight.metaverse.backend.domain.user;

import kr.wiselight.metaverse.backend.controller.dto.qna.QnaSaveRequestDto;
import kr.wiselight.metaverse.backend.domain.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Table(name = "qna")
@Entity
public class Qna extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long qnaId;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Lob
    private String answer;

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String username;

    public Qna(QnaSaveRequestDto dto, User user) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.userId = user.getId();
        this.username = user.getUsername();
    }
}
