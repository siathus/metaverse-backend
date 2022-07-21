package kr.wiselight.metaverse.backend.controller.dto.user;

import lombok.Getter;

@Getter
public class UserUpdateRequestDto {

    private String currentPassword;
    private String updatePassword;
}
