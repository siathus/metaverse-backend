package kr.wiselight.metaverse.backend.controller.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class UserResponseDto {

    private int totalPages;
    private long totalElements;
    private List<UserListResponseDto> content;

    @Builder
    public UserResponseDto(int totalPages, long totalElements, List<UserListResponseDto> content) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.content = content;
    }
}
