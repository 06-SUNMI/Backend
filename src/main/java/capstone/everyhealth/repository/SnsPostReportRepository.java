package capstone.everyhealth.repository;

import capstone.everyhealth.domain.report.SnsPostReport;
import capstone.everyhealth.domain.sns.SnsPost;
import capstone.everyhealth.domain.stakeholder.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SnsPostReportRepository extends JpaRepository<SnsPostReport,Long> {

    Optional<SnsPostReport> findByMemberAndSnsPost(Member member, SnsPost snsPost);
}
