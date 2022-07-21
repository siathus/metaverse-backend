package kr.wiselight.metaverse.backend.service.notice;

import kr.wiselight.metaverse.backend.controller.dto.notice.NoticeRequestDto;
import kr.wiselight.metaverse.backend.controller.dto.notice.NoticeDetailResponseDto;
import kr.wiselight.metaverse.backend.domain.notice.Notice;
import kr.wiselight.metaverse.backend.domain.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Page<Notice> findAll(String keyword, Pageable pageable) {
        return noticeRepository.findAllByKeyword(keyword, pageable);
    }

    public NoticeDetailResponseDto findById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id = " + id));
        return new NoticeDetailResponseDto(notice);
    }

    @Transactional
    public void save(NoticeRequestDto dto) {
        noticeRepository.save(dto.toEntity());
    }

    @Transactional
    public void update(Long id, NoticeRequestDto dto) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id = " + id));

        notice.update(dto);
    }

    @Transactional
    public void delete(Long id) {
        noticeRepository.deleteById(id);
    }
}
