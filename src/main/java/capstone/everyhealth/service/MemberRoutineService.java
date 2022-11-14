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
        return routineRepository.findById(routineId).get();
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

        MemberRoutine memberRoutine = routineRepository.findById(routineId).get();

        memberRoutine.getMemberRoutineContentList().add(memberRoutineContent);
        memberRoutineContent.setMemberRoutine(memberRoutine);

        Long memberRoutineContentId = routineContentRepository.save(memberRoutineContent).getId();

        return memberRoutineContentId;
    }

    @Transactional
    public void deleteWorkout(Long routineId, Long routineContentId) {

        MemberRoutine memberRoutine = routineRepository.findById(routineId).get();

        for (MemberRoutineContent memberRoutineContent : memberRoutine.getMemberRoutineContentList()) {

            if (memberRoutineContent.getId() == routineContentId) {

                memberRoutineContent.setMemberRoutine(null);
                memberRoutine.getMemberRoutineContentList().remove(memberRoutineContent);

                break;
            }
        }
    }

    @Transactional
    public void updateWorkout(Long routineId, Long routineContentId, MemberRoutineWorkoutContent memberRoutineWorkoutContent) {

        MemberRoutine memberRoutine = routineRepository.findById(routineId).get();

        for (MemberRoutineContent memberRoutineContent : memberRoutine.getMemberRoutineContentList()) {

            if (memberRoutineContent.getId() == routineContentId) {

                Workout workout = workoutRepository.findByWorkoutName(memberRoutineWorkoutContent.getMemberRoutineWorkoutName());
                updateWorkoutContent(memberRoutineWorkoutContent, memberRoutineContent, workout);

                break;
            }
        }
    }

    @Transactional
    public void deleteRoutine(Long routineId) {

        MemberRoutine memberRoutine = routineRepository.findById(routineId).get();

        routineRepository.deleteById(routineId);
    }

    private void updateWorkoutContent(MemberRoutineWorkoutContent memberRoutineWorkoutContent, MemberRoutineContent memberRoutineContent, Workout workout) {
        memberRoutineContent.setMemberRoutineWorkoutCount(memberRoutineWorkoutContent.getMemberRoutineWorkoutCount());
        memberRoutineContent.setMemberRoutineWorkoutSet(memberRoutineWorkoutContent.getMemberRoutineWorkoutSet());
        memberRoutineContent.setMemberRoutineWorkoutTime(memberRoutineWorkoutContent.getMemberRoutineWorkoutTime());
        memberRoutineContent.setMemberRoutineWorkoutWeight(memberRoutineWorkoutContent.getMemberRoutineWorkoutWeight());
        memberRoutineContent.setWorkout(workout);
    }
}
