package capstone.everyhealth.repository;

import capstone.everyhealth.domain.report.ChallengeAuthPostReportPunishment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeAuthPostReportPunishmentRepository extends JpaRepository<ChallengeAuthPostReportPunishment, Long> {
}
