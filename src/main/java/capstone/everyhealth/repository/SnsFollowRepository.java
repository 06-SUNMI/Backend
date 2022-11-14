package capstone.everyhealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import capstone.everyhealth.domain.sns.SnsFollowing;
import capstone.everyhealth.domain.stakeholder.Member;

public interface SnsFollowRepository extends JpaRepository<SnsFollowing, Long> {

    void deleteByFollowedMemberAndFollowingMember(Member followedMember, Member followingMember);

}