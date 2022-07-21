package kr.wiselight.metaverse.backend.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Setter
@NoArgsConstructor
public class OpenSimServerRequestDto {

    @JsonProperty(value = "METHOD")
    private String method;

    @JsonProperty(value = "FirstName")
    private String firstName;

    @JsonProperty(value = "LastName")
    private String lastName;

    @JsonProperty(value = "Password")
    private String password;

    @JsonProperty(value = "PrincipalID")
    private String principalId;

    @Builder
    public OpenSimServerRequestDto(String method, String firstName, String password, String principalId) {
        this.method = method;
        this.firstName = firstName;
        this.lastName = "USER";
        this.password = password;
        this.principalId = principalId;
    }
}
