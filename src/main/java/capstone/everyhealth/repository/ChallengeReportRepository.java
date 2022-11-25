package capstone.everyhealth.repository;

import capstone.everyhealth.domain.report.ChallengeAuthPostReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeReportRepository extends JpaRepository<ChallengeAuthPostReport, Long> {

}
