package kr.wiselight.metaverse.backend.service.qna;

import kr.wiselight.metaverse.backend.controller.dto.qna.*;
import kr.wiselight.metaverse.backend.domain.user.Qna;
import kr.wiselight.metaverse.backend.domain.user.QnaRepository;
import kr.wiselight.metaverse.backend.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class QnaService {

    private final QnaRepository qnaRepository;

    public AdminQnaResponseDto findAll(Pageable pageable) {
        Page<Qna> qnaList = qnaRepository.findByPageWithUser(pageable);

        return AdminQnaResponseDto.builder()
                .totalPages(qnaList.getTotalPages())
                .totalElements(qnaList.getTotalElements())
                .qnaList(qnaList.getContent())
                .build();
    }

    public UserQnaResponseDto findByUserId(User user, Pageable pageable) {
        Page<Qna> page = qnaRepository.findAllByUserId(user.getId(), pageable);
        List<Qna> qnaList = page.getContent();

        return UserQnaResponseDto.builder()
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .qnaList(qnaList)
                .build();
    }

    @Transactional
    public void save(QnaSaveRequestDto dto, User user) {
        qnaRepository.save(new Qna(dto, user));
    }

    @Transactional
    public void reply(QnaReplyRequestDto dto) {
        Qna qna = qnaRepository.getById(dto.getQnaId());
        qna.setAnswer(dto.getAnswer());
    }

    @Transactional
    public void deleteByUserId(String userId) {
        qnaRepository.deleteAllByUserId(userId);
    }
}
