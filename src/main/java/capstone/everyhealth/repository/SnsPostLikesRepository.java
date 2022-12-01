package capstone.everyhealth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import capstone.everyhealth.domain.report.SnsCommentReport;
import capstone.everyhealth.domain.sns.SnsPost;
import capstone.everyhealth.domain.sns.SnsPostLikes;
import capstone.everyhealth.domain.stakeholder.Member;

@Repository
public interface SnsPostLikesRepository extends JpaRepository <SnsPostLikes, Long>{

    Optional<SnsPostLikes> findBySnsPostAndMember(SnsPost snsPost, Member member);

}
