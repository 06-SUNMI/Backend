package capstone.everyhealth.repository;

import capstone.everyhealth.domain.report.SnsCommentReport;
import capstone.everyhealth.domain.sns.SnsComment;
import capstone.everyhealth.domain.stakeholder.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SnsCommentReportRepository extends JpaRepository<SnsCommentReport, Long> {

    Optional<SnsCommentReport> findByMemberAndSnsComment(Member member, SnsComment snsComment);
}
