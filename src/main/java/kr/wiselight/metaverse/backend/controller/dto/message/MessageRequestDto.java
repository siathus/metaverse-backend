package kr.wiselight.metaverse.backend.controller.dto.message;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageRequestDto {
    private String message;

    @Builder
    public MessageRequestDto(String message) {
        this.message = message;
    }
}
