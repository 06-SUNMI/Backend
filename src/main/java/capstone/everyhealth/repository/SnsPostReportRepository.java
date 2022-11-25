package capstone.everyhealth.repository;

import capstone.everyhealth.domain.report.SnsPostReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnsPostReportRepository extends JpaRepository<SnsPostReport,Long> {

}
