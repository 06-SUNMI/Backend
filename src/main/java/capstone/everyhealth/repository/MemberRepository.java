package capstone.everyhealth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import capstone.everyhealth.domain.stakeholder.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long>{

    List<Member> findAllByName(String userName);

    List<Member> findAllByGymIdAndIdNot(String gymId, Long id);
}
