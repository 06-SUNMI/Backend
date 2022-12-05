package capstone.everyhealth.repository;

import capstone.everyhealth.domain.sns.SnsPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import capstone.everyhealth.domain.sns.SnsComment;

import java.util.List;

@Repository
public interface SnsCommentRepository extends JpaRepository<SnsComment, Long> {
    List<SnsComment> findAllBySnsPost(SnsPost snsPost);
}
