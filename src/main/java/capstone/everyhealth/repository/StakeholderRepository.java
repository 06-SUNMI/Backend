package capstone.everyhealth.repository;

import capstone.everyhealth.domain.stakeholder.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StakeholderRepository extends JpaRepository<Member, Long> {


}
