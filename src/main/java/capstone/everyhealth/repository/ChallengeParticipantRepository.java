package capstone.everyhealth.repository;

import capstone.everyhealth.domain.challenge.Challenge;
import capstone.everyhealth.domain.challenge.ChallengeParticipant;
import capstone.everyhealth.domain.stakeholder.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, Long> {
    List<ChallengeParticipant> findByMember(Member member);

    Optional<ChallengeParticipant> findByChallengeAndMember(Challenge challenge, Member member);

    List<ChallengeParticipant> findAllByChallenge(Challenge challenge);

    List<ChallengeParticipant> findAllByMember(Member member);
}
