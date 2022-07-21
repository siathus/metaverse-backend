package kr.wiselight.metaverse.backend.domain.token;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshToken {

    private String username;

    private String token;

    @Builder
    public RefreshToken(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public void update(String token) {
        this.token = token;
    }
}
