package capstone.everyhealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import capstone.everyhealth.domain.sns.SnsFollowing;
import capstone.everyhealth.domain.stakeholder.Member;

import java.util.List;
import java.util.Optional;

public interface SnsFollowRepository extends JpaRepository<SnsFollowing, Long> {

    void deleteByFollowedMemberAndFollowingMember(Member followedMember, Member followingMember);

    List<SnsFollowing> findByFollowingMember(Member member);

    Optional<SnsFollowing> findByFollowedMemberAndFollowingMember(Member followedMember, Member followingMember);
}
