package kr.wiselight.metaverse.backend.component.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserTokenDto {

    private String accessToken;
    private String refreshToken;

    @Builder
    public UserTokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
