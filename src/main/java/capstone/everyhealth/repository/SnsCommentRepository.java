package capstone.everyhealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import capstone.everyhealth.domain.sns.SnsComment;

@Repository
public interface SnsCommentRepository extends JpaRepository<SnsComment, Long> {

    
}
