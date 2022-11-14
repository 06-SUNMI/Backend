package capstone.everyhealth.repository;

import capstone.everyhealth.domain.challenge.ChallengeParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, Long> {
}
