package capstone.everyhealth.repository;

import capstone.everyhealth.domain.stakeholder.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public interface StakeholderRepository extends JpaRepository<Member, Long> {


}
