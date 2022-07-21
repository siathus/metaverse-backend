package kr.wiselight.metaverse.backend.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel
@Getter
@NoArgsConstructor
public class LoginRequestDto {

    @ApiModelProperty(value = "사용자명", required = true)
    private String username;
    private String password;
}
