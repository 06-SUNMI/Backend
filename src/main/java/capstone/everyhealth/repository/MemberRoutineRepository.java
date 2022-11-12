package capstone.everyhealth.repository;

import capstone.everyhealth.domain.routine.MemberRoutine;
import capstone.everyhealth.domain.stakeholder.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRoutineRepository extends JpaRepository<MemberRoutine,Long> {

    List<MemberRoutine> findByMember(Member member);
    Optional<MemberRoutine> findById(Long routineId);
    void deleteById(Long routineId);
}
