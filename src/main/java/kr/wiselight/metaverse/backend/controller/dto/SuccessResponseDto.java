package kr.wiselight.metaverse.backend.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SuccessResponseDto {

    private Boolean success;

    @Builder
    public SuccessResponseDto(Boolean success) {
        this.success = success;
    }
}
