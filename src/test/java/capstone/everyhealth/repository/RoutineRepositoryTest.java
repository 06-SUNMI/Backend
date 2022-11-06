package capstone.everyhealth.repository;

import capstone.everyhealth.domain.routine.MemberRoutine;
import capstone.everyhealth.domain.routine.MemberRoutineContent;
import capstone.everyhealth.domain.routine.Workout;
import capstone.everyhealth.domain.stakeholder.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@Disabled
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
class RoutineRepositoryTest {

    /*
    @Autowired
    private MemberRoutineRepository routineRepository;

    @Test
    @DisplayName("루틴 등록 레포지토리 단위 테스트")
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

        // when
        MemberRoutine savedRoutine = routineRepository.save(routine);
        Optional<MemberRoutine> foundRoutine = routineRepository.findById(savedRoutine.getId());

        // then
        assertEquals(savedRoutine.getId(), foundRoutine.get().getId());
    }

    private MemberRoutineContent getSecondContent(Workout secondWorkout, Workout workout) {
        return MemberRoutineContent.builder()
                .workout(secondWorkout)
                .count(null)
                .weight(null)
                .set(null)
                .time(120)
                .build();
    }

    private MemberRoutineContent getFirstContent(Workout firstWorkout, Workout workout) {
        return MemberRoutineContent.builder()
                .workout(firstWorkout)
                .count(10)
                .weight(100)
                .set(2)
                .time(null)
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

        Member member = Member.builder()
                .id(1L)
                .build();

        return MemberRoutine.builder()
                .member(new Member())
                .date("2022-12-12")
                .routineContentList(routineContentList)
                .build();
    }

     */
}
