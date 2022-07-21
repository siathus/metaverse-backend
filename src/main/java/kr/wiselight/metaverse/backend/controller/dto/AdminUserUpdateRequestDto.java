package kr.wiselight.metaverse.backend.controller.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AdminUserUpdateRequestDto {

    private String username;
    private String password;
    private String email;
}
