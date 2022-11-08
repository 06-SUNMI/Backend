package capstone.everyhealth.service;

import capstone.everyhealth.domain.stakeholder.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {


}
