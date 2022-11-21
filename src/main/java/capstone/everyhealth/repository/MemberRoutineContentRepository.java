package capstone.everyhealth.repository;

import capstone.everyhealth.domain.routine.MemberRoutineContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRoutineContentRepository extends JpaRepository<MemberRoutineContent, Long> {
}
