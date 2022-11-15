package capstone.everyhealth.repository;

import capstone.everyhealth.domain.challenge.ChallengeRoutine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRoutineRepository extends JpaRepository<ChallengeRoutine, Long> {


}
