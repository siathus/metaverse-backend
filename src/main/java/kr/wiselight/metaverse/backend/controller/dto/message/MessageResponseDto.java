package kr.wiselight.metaverse.backend.controller.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class MessageResponseDto {

    private boolean success;
    private boolean accepted;
    private String error;
}
