package capstone.everyhealth.repository;

import capstone.everyhealth.domain.challenge.ChallengeAuthPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeAuthPostRepository extends JpaRepository<ChallengeAuthPost, Long> {
}
