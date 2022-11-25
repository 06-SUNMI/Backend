package capstone.everyhealth.repository;

import capstone.everyhealth.domain.challenge.ChallengeParticipant;
import capstone.everyhealth.domain.challenge.ChallengeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeTransactionRepository extends JpaRepository<ChallengeTransaction, Long> {
    List<ChallengeTransaction> findAllByChallengeParticipantIn(List<ChallengeParticipant> challengeParticipantList);
}
