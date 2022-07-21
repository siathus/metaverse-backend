package kr.wiselight.metaverse.backend.controller.dto.qna;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QnaReplyRequestDto {

    private Long qnaId;
    private String answer;
}
