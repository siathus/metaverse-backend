package kr.wiselight.metaverse.backend.controller.dto.user;

import kr.wiselight.metaverse.backend.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserListResponseDto {
    private String id;
    private String username;
    private String email;

    public UserListResponseDto(User entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.email = entity.getEmail();
    }
}
