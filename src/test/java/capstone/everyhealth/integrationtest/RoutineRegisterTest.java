package capstone.everyhealth.integrationtest;

import capstone.everyhealth.controller.MemberRoutineController;
import capstone.everyhealth.controller.dto.MemberRoutineRegisterRequest;
import capstone.everyhealth.controller.dto.MemberRoutineWorkoutContent;
import capstone.everyhealth.domain.routine.WorkoutName;
import capstone.everyhealth.repository.MemberRoutineRepository;
import capstone.everyhealth.repository.StakeholderRepository;
import capstone.everyhealth.repository.WorkoutRepository;
import capstone.everyhealth.service.MemberRoutineService;
import capstone.everyhealth.service.StakeholderService;
import capstone.everyhealth.service.WorkoutService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MemberRoutineController.class)
@ActiveProfiles("test")
@Slf4j
public class RoutineRegisterTest {

//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private MemberRoutineService memberRoutineService;
//
//    @MockBean
//    private WorkoutService workoutService;
//
//    @MockBean
//    private StakeholderService stakeholderService;
//
//    @Test
//    void routineRegisterIntegrationTest() throws Exception {
//
//        // given
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        LocalDate routineRegisterdate = LocalDate.now();
//        MemberRoutineWorkoutContent memberRoutineWorkoutContent1 = new MemberRoutineWorkoutContent();
//        MemberRoutineWorkoutContent memberRoutineWorkoutContent2 = new MemberRoutineWorkoutContent();
//
//        setMemberRoutineWorkoutContents(memberRoutineWorkoutContent1, memberRoutineWorkoutContent2);
//
//        List<MemberRoutineWorkoutContent> memberRoutineWorkoutContentList = new ArrayList<>();
//
//        memberRoutineWorkoutContentList.add(memberRoutineWorkoutContent1);
//        memberRoutineWorkoutContentList.add(memberRoutineWorkoutContent2);
//
//        MemberRoutineRegisterRequest memberRoutineRegisterRequest = createMemberRoutineRegisterRequest(routineRegisterdate, memberRoutineWorkoutContentList);
//
//        // when & then
//        mvc.perform(MockMvcRequestBuilders
//                        .post("/members/1/routines")
//                        .content(objectMapper.registerModule(new JavaTimeModule()).writeValueAsString(memberRoutineRegisterRequest))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk());
//
//
//        // then
//    }
//
//    private MemberRoutineRegisterRequest createMemberRoutineRegisterRequest(LocalDate routineRegisterdate, List<MemberRoutineWorkoutContent> memberRoutineWorkoutContentList) {
//        return MemberRoutineRegisterRequest.builder()
//                .memberRoutineWorkoutContentList(memberRoutineWorkoutContentList)
//                .routineRegisterdate(routineRegisterdate)
//                .build();
//    }
//
//    private void setMemberRoutineWorkoutContents(MemberRoutineWorkoutContent memberRoutineWorkoutContent1, MemberRoutineWorkoutContent memberRoutineWorkoutContent2) {
//        memberRoutineWorkoutContent1.setMemberRoutineWorkoutCount(20);
//        memberRoutineWorkoutContent1.setMemberRoutineWorkoutName(WorkoutName.BENCH_PRESS_DUMBBELL);
//        memberRoutineWorkoutContent1.setMemberRoutineWorkoutSet(10);
//        memberRoutineWorkoutContent1.setMemberRoutineWorkoutTime(null);
//        memberRoutineWorkoutContent1.setMemberRoutineWorkoutWeight(100);
//
//        memberRoutineWorkoutContent2.setMemberRoutineWorkoutCount(null);
//        memberRoutineWorkoutContent2.setMemberRoutineWorkoutName(WorkoutName.PLANK);
//        memberRoutineWorkoutContent2.setMemberRoutineWorkoutSet(null);
//        memberRoutineWorkoutContent2.setMemberRoutineWorkoutTime(100);
//        memberRoutineWorkoutContent2.setMemberRoutineWorkoutWeight(null);
//    }
}
