package capstone.everyhealth.repository;

import capstone.everyhealth.domain.challenge.ChallengeAuthPost;
import capstone.everyhealth.domain.report.ChallengeAuthPostReport;
import capstone.everyhealth.domain.stakeholder.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChallengeAuthPostReportRepository extends JpaRepository<ChallengeAuthPostReport, Long> {

    Optional<ChallengeAuthPostReport> findByChallengeAuthPostAndMember(ChallengeAuthPost challengeAuthPost, Member member);
}
