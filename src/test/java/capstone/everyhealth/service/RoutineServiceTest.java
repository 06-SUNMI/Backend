package capstone.everyhealth.service;

import capstone.everyhealth.domain.routine.MemberRoutine;
import capstone.everyhealth.domain.routine.MemberRoutineContent;
import capstone.everyhealth.domain.routine.Workout;
import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.repository.MemberRoutineRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Disabled
@ExtendWith(SpringExtension.class)
@Slf4j
class RoutineServiceTest {

    /*@InjectMocks
    private MemberRoutineService routineService;

    @Mock
    private MemberRoutineRepository routineRepository;

    @Test
    @DisplayName("루틴 등록 서비스 단위 테스트")
    void routineRegisterTest() {

        // given
        MemberRoutine routine;
        List<MemberRoutineContent> contentList = new ArrayList<>();

        Workout firstWorkout = getFirstWorkout();
        Workout secondWorkout = getSecondWorkout();

        MemberRoutineContent firstContent = getFirstContent(firstWorkout, firstWorkout);
        MemberRoutineContent secondContent = getSecondContent(secondWorkout, secondWorkout);

        contentList.add(firstContent);
        contentList.add(secondContent);

        routine = getRoutine(contentList);

        when(routineRepository.save(any(MemberRoutine.class))).thenReturn(MemberRoutine.builder().id(1L).build());

        // when
        Long savedRoutineId = routineService.save(routine);

        // then
        assertEquals(savedRoutineId, 1L);
    }

    private MemberRoutineContent getSecondContent(Workout secondWorkout, Workout workout) {
        return MemberRoutineContent.builder()
                .workout(secondWorkout)
                .numCount(null)
                .weight(null)
                .numSet(null)
                .numTime(120)
                .build();
    }

    private MemberRoutineContent getFirstContent(Workout firstWorkout, Workout workout) {
        return MemberRoutineContent.builder()
                .workout(firstWorkout)
                .numCount(10)
                .weight(100)
                .numSet(2)
                .numTime(null)
                .build();
    }

    private Workout getSecondWorkout() {
        return Workout.builder()
                .target("코어")
                .type("플랭크")
                .build();
    }

    private Workout getFirstWorkout() {
        return Workout.builder()
                .target("등")
                .type("렛 풀다운")
                .build();
    }

    private MemberRoutine getRoutine(List<MemberRoutineContent> routineContentList) {
        return MemberRoutine.builder()
                .member(new Member())
                .date("2022-12-12")
                .routineContentList(routineContentList)
                .build();
    }

     */
}
