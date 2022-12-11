package capstone.everyhealth.repository;

import capstone.everyhealth.domain.challenge.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {


    List<Challenge> findByEndDateGreaterThanEqual(LocalDate now);
    List<Challenge> findByEndDateLessThan(LocalDate now);

    List<Challenge> findByIsFinished(boolean b);
}
