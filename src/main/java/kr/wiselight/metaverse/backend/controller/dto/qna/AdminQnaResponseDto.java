package kr.wiselight.metaverse.backend.controller.dto.qna;

import kr.wiselight.metaverse.backend.domain.user.Qna;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class AdminQnaResponseDto {

    private int totalPages;
    private long totalElements;
    private List<AdminQnaListResponseDto> qnaList = new ArrayList<>();

    @Builder
    public AdminQnaResponseDto(int totalPages, long totalElements, List<Qna> qnaList) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.qnaList = qnaList.stream()
                .map(AdminQnaListResponseDto::new)
                .collect(Collectors.toList());
    }
}
