package kr.wiselight.metaverse.backend.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponseDto {
    private String message;

    @Builder
    public ErrorResponseDto(String message) {
        this.message = message;
    }
}
