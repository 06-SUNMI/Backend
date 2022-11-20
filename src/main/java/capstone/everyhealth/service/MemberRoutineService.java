package capstone.everyhealth.service;

import capstone.everyhealth.controller.dto.MemberRoutine.MemberRoutineWorkoutContent;
import capstone.everyhealth.domain.routine.MemberRoutine;
import capstone.everyhealth.domain.routine.MemberRoutineContent;
import capstone.everyhealth.domain.routine.Workout;
import capstone.everyhealth.domain.stakeholder.Member;
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
    public Long save(MemberRoutine routine) {

        return routineRepository.save(routine).getId();
    }

    public List<MemberRoutine> findAllRoutines(Long memberId) {

        Member member = memberRepository.findById(memberId).get();

        return routineRepository.findByMember(member);
    }

    public MemberRoutine findRoutineByRoutineId(Long routineId) {

        MemberRoutine memberRoutine = routineRepository.findById(routineId).get();

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
    public Long addWorkout(Long routineId, MemberRoutineContent memberRoutineContent) {

        MemberRoutine savedMemberRoutine = routineRepository.findById(routineId).get();
        if (validateIsRoutineFromChallenge(savedMemberRoutine)) {
            return -1L;
        }

        MemberRoutine memberRoutine = routineRepository.findById(routineId).get();

        memberRoutine.getMemberRoutineContentList().add(memberRoutineContent);
        memberRoutineContent.setMemberRoutine(memberRoutine);

        Long memberRoutineContentId = routineContentRepository.save(memberRoutineContent).getId();

        //return memberRoutineContentId;
        return 1L;
    }

    @Transactional
    public Long deleteWorkout(Long routineId, Long routineContentId) {

        log.info("routineId = {}", routineId);
        log.info("routineContentId = {}", routineContentId);

        MemberRoutine memberRoutine = routineRepository.findById(routineId).get();
        log.info("memberRoutine ID = {}", memberRoutine.getId());

        if (validateIsRoutineFromChallenge(memberRoutine)) {
            return -1L;
        }

        for (MemberRoutineContent memberRoutineContent : memberRoutine.getMemberRoutineContentList()) {
            log.info("memberRoutineContent.getId() = {}, type = {}", memberRoutineContent.getId(), memberRoutineContent.getId().getClass());
            log.info("routineContentId = {}, type = {}", routineContentId, routineContentId.getClass());
            log.info("memberRoutineContent.getId() == routineContentId = {}", memberRoutineContent.getId() == routineContentId);
            if (memberRoutineContent.getId().equals(routineContentId)) {
                log.info("routineContentId = {}", routineContentId);
                memberRoutine.getMemberRoutineContentList().remove(memberRoutineContent);
                routineContentRepository.delete(memberRoutineContent);

                break;
            }
        }
        return 1L;
    }

    @Transactional
    public Long updateWorkout(Long routineContentId, MemberRoutineWorkoutContent memberRoutineWorkoutContent) {

        MemberRoutine memberRoutine = routineContentRepository.findById(routineContentId).get().getMemberRoutine();

        if (validateIsRoutineFromChallenge(memberRoutine)) {
            return -1L;
        }

        MemberRoutineContent memberRoutineContent = routineContentRepository.findById(routineContentId).get();

        Workout workout = workoutRepository.findByWorkoutName(memberRoutineWorkoutContent.getMemberRoutineWorkoutName());
        updateWorkoutContent(memberRoutineWorkoutContent, memberRoutineContent, workout);

        return 1L;
    }

    @Transactional
    public Long deleteRoutine(Long routineId) {

        MemberRoutine memberRoutine = routineRepository.findById(routineId).get();

        if (validateIsRoutineFromChallenge(memberRoutine)) {
            return -1L;
        }

        routineRepository.deleteById(routineId);
        return 1L;
    }

    @Transactional
    public void updateRoutineContentCheck(Long routineContentId) {

        MemberRoutineContent memberRoutineContent = routineContentRepository.findById(routineContentId).get();
        boolean currentCheckStatus = memberRoutineContent.isMemberRoutineIsChecked();

        memberRoutineContent.setMemberRoutineIsChecked(!currentCheckStatus);
    }

    private boolean validateIsRoutineFromChallenge(MemberRoutine memberRoutine) {
        return memberRoutine.getChallengeRoutine() != null;
    }

    private void updateWorkoutContent(MemberRoutineWorkoutContent memberRoutineWorkoutContent, MemberRoutineContent memberRoutineContent, Workout workout) {
        memberRoutineContent.setMemberRoutineWorkoutCount(memberRoutineWorkoutContent.getMemberRoutineWorkoutCount());
        memberRoutineContent.setMemberRoutineWorkoutSet(memberRoutineWorkoutContent.getMemberRoutineWorkoutSet());
        memberRoutineContent.setMemberRoutineWorkoutTime(memberRoutineWorkoutContent.getMemberRoutineWorkoutTime());
        memberRoutineContent.setMemberRoutineWorkoutWeight(memberRoutineWorkoutContent.getMemberRoutineWorkoutWeight());
        memberRoutineContent.setWorkout(workout);
    }
}
