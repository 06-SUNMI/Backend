package capstone.everyhealth.service;

import capstone.everyhealth.controller.dto.MemberRoutine.MemberRoutineWorkoutContent;
import capstone.everyhealth.domain.routine.MemberRoutine;
import capstone.everyhealth.domain.routine.MemberRoutineContent;
import capstone.everyhealth.domain.routine.Workout;
import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.exception.memberroutine.ChallengeRoutineNotEditable;
import capstone.everyhealth.exception.memberroutine.MemberRoutineContentNotFound;
import capstone.everyhealth.exception.memberroutine.MemberRoutineNotFound;
import capstone.everyhealth.exception.memberroutine.WorkoutNotFound;
import capstone.everyhealth.exception.stakeholder.MemberNotFound;
import capstone.everyhealth.repository.MemberRepository;
import capstone.everyhealth.repository.MemberRoutineContentRepository;
import capstone.everyhealth.repository.MemberRoutineRepository;
import capstone.everyhealth.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberRoutineService {
    private final MemberRoutineRepository routineRepository;
    private final MemberRepository memberRepository;
    private final MemberRoutineContentRepository routineContentRepository;
    private final WorkoutRepository workoutRepository;


    @Transactional
    public Long save(MemberRoutine routine) throws WorkoutNotFound {

        validateIsWorkoutIncludedInRoutine(routine);

        return routineRepository.save(routine).getId();
    }

    public List<MemberRoutine> findAllRoutines(Long memberId) throws MemberNotFound {

        Member member = memberRepository.findById(memberId).orElseThrow(()->new MemberNotFound(memberId));

        return routineRepository.findByMember(member);
    }

    public MemberRoutine findRoutineByRoutineId(Long routineId) throws MemberRoutineNotFound {

        MemberRoutine memberRoutine = routineRepository.findById(routineId).orElseThrow(() -> new MemberRoutineNotFound(routineId));

        return memberRoutine;
    }

    /*@Transactional
    public void updateRoutine(Long routineId, List<MemberRoutineContent> memberRoutineContentList) {

        MemberRoutine memberRoutine = routineRepository.findById(routineId).get();

        memberRoutine.getMemberRoutineContentList().clear();

        for (MemberRoutineContent memberRoutineContent : memberRoutineContentList){
            memberRoutineContent.addMemberRoutine(memberRoutine);
        }
    }*/

    @Transactional
    public Long addWorkout(Long routineId, MemberRoutineContent memberRoutineContent) throws MemberRoutineNotFound, ChallengeRoutineNotEditable, WorkoutNotFound {

        MemberRoutine memberRoutine = routineRepository.findById(routineId).orElseThrow(()->new MemberRoutineNotFound(routineId));

        validateIsWorkoutIncludedInRoutine(memberRoutine);
        validateIsRoutineFromChallenge(memberRoutine);

        memberRoutine.getMemberRoutineContentList().add(memberRoutineContent);
        memberRoutineContent.setMemberRoutine(memberRoutine);

        Long memberRoutineContentId = routineContentRepository.save(memberRoutineContent).getId();

        return memberRoutineContentId;
    }

    @Transactional
    public Long deleteWorkout(Long routineId, Long routineContentId) throws ChallengeRoutineNotEditable, MemberRoutineNotFound {

        log.info("routineId = {}", routineId);
        log.info("routineContentId = {}", routineContentId);

        MemberRoutine memberRoutine = routineRepository.findById(routineId).orElseThrow(()-> new MemberRoutineNotFound(routineId));

        validateIsRoutineFromChallenge(memberRoutine);

        for (MemberRoutineContent memberRoutineContent : memberRoutine.getMemberRoutineContentList()) {

            if (memberRoutineContent.getId().equals(routineContentId)) {

                memberRoutine.getMemberRoutineContentList().remove(memberRoutineContent);
                routineContentRepository.delete(memberRoutineContent);

                break;
            }
        }
        return routineContentId;
    }

    @Transactional
    public Long updateWorkout(Long routineContentId, MemberRoutineWorkoutContent memberRoutineWorkoutContent) throws ChallengeRoutineNotEditable, MemberRoutineContentNotFound, MemberRoutineNotFound {

        MemberRoutine memberRoutine = routineContentRepository.findById(routineContentId).orElseThrow(()-> new MemberRoutineContentNotFound(routineContentId)).getMemberRoutine();

        validateIsRoutineFromChallenge(memberRoutine);

        MemberRoutineContent memberRoutineContent = routineContentRepository.findById(routineContentId).orElseThrow(()->new MemberRoutineContentNotFound(routineContentId));

        Workout workout = workoutRepository.findByWorkoutName(memberRoutineWorkoutContent.getMemberRoutineWorkoutName());
        updateWorkoutContent(memberRoutineWorkoutContent, memberRoutineContent, workout);

        return 1L;
    }

    @Transactional
    public Long deleteRoutine(Long routineId) throws ChallengeRoutineNotEditable, MemberRoutineNotFound {

        MemberRoutine memberRoutine = routineRepository.findById(routineId).orElseThrow(()->new MemberRoutineNotFound(routineId));

        validateIsRoutineFromChallenge(memberRoutine);

        routineRepository.deleteById(routineId);
        return 1L;
    }

    @Transactional
    public void updateRoutineContentCheck(Long routineContentId) throws MemberRoutineContentNotFound {

        MemberRoutineContent memberRoutineContent = routineContentRepository.findById(routineContentId).orElseThrow(()->new MemberRoutineContentNotFound(routineContentId));
        MemberRoutine memberRoutine = memberRoutineContent.getMemberRoutine();
        boolean currentCheckStatus = memberRoutineContent.isMemberRoutineIsChecked();

        memberRoutineContent.setMemberRoutineIsChecked(!currentCheckStatus);
        memberRoutine.calculateAndSetProgressRate();
    }

    private void validateIsWorkoutIncludedInRoutine(MemberRoutine routine) throws WorkoutNotFound {
        for (MemberRoutineContent memberRoutineContent : routine.getMemberRoutineContentList()) {

            if (memberRoutineContent.getWorkout() == null) {
                throw new WorkoutNotFound();
            }
        }
    }

    private void validateIsRoutineFromChallenge(MemberRoutine memberRoutine) throws ChallengeRoutineNotEditable {
        if (memberRoutine.getChallengeRoutine() != null){
            throw new ChallengeRoutineNotEditable();
        }
    }

    private void updateWorkoutContent(MemberRoutineWorkoutContent memberRoutineWorkoutContent, MemberRoutineContent memberRoutineContent, Workout workout) {
        memberRoutineContent.setMemberRoutineWorkoutCount(memberRoutineWorkoutContent.getMemberRoutineWorkoutCount());
        memberRoutineContent.setMemberRoutineWorkoutSet(memberRoutineWorkoutContent.getMemberRoutineWorkoutSet());
        memberRoutineContent.setMemberRoutineWorkoutTime(memberRoutineWorkoutContent.getMemberRoutineWorkoutTime());
        memberRoutineContent.setMemberRoutineWorkoutWeight(memberRoutineWorkoutContent.getMemberRoutineWorkoutWeight());
        memberRoutineContent.setWorkout(workout);
    }
}
