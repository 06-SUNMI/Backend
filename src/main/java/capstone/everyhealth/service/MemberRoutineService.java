package capstone.everyhealth.service;

import capstone.everyhealth.domain.routine.MemberRoutine;
import capstone.everyhealth.domain.routine.MemberRoutineContent;
import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.repository.MemberRoutineContentRepository;
import capstone.everyhealth.repository.MemberRoutineRepository;
import capstone.everyhealth.repository.StakeholderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberRoutineService {
    private final MemberRoutineRepository routineRepository;
    private final StakeholderRepository stakeholderRepository;


    @Transactional
    public Long save(MemberRoutine routine) {
        return routineRepository.save(routine).getId();
    }

    public List<MemberRoutine> findAllRoutines(Long memberId) {

        Member member = stakeholderRepository.findById(memberId).get();

        return routineRepository.findByMember(member);
    }

    public MemberRoutine findRoutineByRoutineId(Long routineId) {
        return routineRepository.findById(routineId).get();
    }

    @Transactional
    public void updateRoutine(Long routineId, List<MemberRoutineContent> memberRoutineContentList) {

        MemberRoutine memberRoutine = routineRepository.findById(routineId).get();

//        for (MemberRoutineContent memberRoutineContent : memberRoutine.getMemberRoutineContentList()) {
//            routineContentRepository.delete(memberRoutineContent);
//        }

        memberRoutine.getMemberRoutineContentList().clear();

        for (MemberRoutineContent memberRoutineContent : memberRoutineContentList){
            memberRoutineContent.addMemberRoutine(memberRoutine);
        }
    }

    @Transactional
    public void deleteRoutine(Long routineId) {

        MemberRoutine memberRoutine = routineRepository.findById(routineId).get();

        routineRepository.deleteById(routineId);
    }
}
