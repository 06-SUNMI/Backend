package capstone.everyhealth.repository;

import capstone.everyhealth.domain.challenge.ChallengeAuthPost;
import capstone.everyhealth.domain.challenge.ChallengeRoutine;
import capstone.everyhealth.domain.stakeholder.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeAuthPostRepository extends JpaRepository<ChallengeAuthPost, Long> {
    ChallengeAuthPost findByMemberAndChallengeRoutine(Member member, ChallengeRoutine challengeRoutine);
}
