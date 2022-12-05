package capstone.everyhealth.repository;

import capstone.everyhealth.domain.report.SnsCommentReportPunishment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnsCommentReportPunishmentRepository extends JpaRepository<SnsCommentReportPunishment, Long> {
}
