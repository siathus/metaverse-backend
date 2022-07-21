package kr.wiselight.metaverse.backend.controller.dto.qna;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class QnaSaveRequestDto {

    private String title;
    private String content;
}
