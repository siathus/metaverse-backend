package kr.wiselight.metaverse.backend.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class RefreshRequestDto {
    @NotNull
    private String accessToken;

    @NotNull
    private String refreshToken;
}
