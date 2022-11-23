package capstone.everyhealth.repository;

import capstone.everyhealth.domain.report.SnsCommentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnsCommentReportRepository extends JpaRepository<SnsCommentReport, Long> {

}
