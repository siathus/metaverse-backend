package kr.wiselight.metaverse.backend.controller.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.wiselight.metaverse.backend.domain.user.Role;
import kr.wiselight.metaverse.backend.domain.user.User;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ApiModel(value = "회원가입 정보")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupDto {

    @NotBlank
    @Pattern(message = "6자 이상 20자 미만, 영문과 숫자 필수", regexp = "^(?!.*[!@#$%^&*()_+=|<>?:{} ])(?=.*[a-zA-Z]{0,20})(?=.*[0-9]{0,20}).{6,20}")
    private String username;

    @NotBlank
    @Pattern(message = "6자 이상 20자 미만, 영문과 숫자 필수", regexp = "^(?=.*[!@#$%^&*()_+=|<>?:{}]{0,20})(?=.*[a-zA-Z])(?=.*[0-9]).{6,20}")
    private String password;

    @ApiModelProperty(value = "이메일", required = true, example = "useremail@gmail.com")
    @Email
    @NotBlank
    private String email;

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .role(Role.USER)
                .build();
    }
}
