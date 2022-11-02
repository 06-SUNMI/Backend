package capstone.everyhealth.service;

import capstone.everyhealth.domain.routine.MemberRoutine;
import capstone.everyhealth.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;

    @Transactional
    public Long save(MemberRoutine routine) {

        return routineRepository.save(routine).getId();
    }
}
