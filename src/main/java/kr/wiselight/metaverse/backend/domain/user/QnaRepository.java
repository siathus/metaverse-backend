package kr.wiselight.metaverse.backend.domain.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QnaRepository extends JpaRepository<Qna, Long> {

//    @EntityGraph(attributePaths = "user")
    @Query("SELECT q FROM Qna q")
    Page<Qna> findByPageWithUser(Pageable pageable);

    Page<Qna> findAllByUserId(String userId, Pageable pageable);

    void deleteAllByUserId(String userId);
}
