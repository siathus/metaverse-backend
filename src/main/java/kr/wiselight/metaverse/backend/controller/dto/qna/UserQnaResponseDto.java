package kr.wiselight.metaverse.backend.controller.dto.qna;

import kr.wiselight.metaverse.backend.domain.user.Qna;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class UserQnaResponseDto {

    private int totalPages;
    private long totalElements;
    private List<UserQnaListResponseDto> qnaList = new ArrayList<>();

    @Builder
    public UserQnaResponseDto(int totalPages, long totalElements, List<Qna> qnaList) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.qnaList = qnaList.stream()
                .map(UserQnaListResponseDto::new)
                .collect(Collectors.toList());
    }
}
