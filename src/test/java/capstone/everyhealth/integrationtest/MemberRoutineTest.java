package capstone.everyhealth.integrationtest;

import capstone.everyhealth.controller.MemberRoutineController;
import capstone.everyhealth.controller.dto.MemberRoutine.MemberRoutineRegisterRequest;
import capstone.everyhealth.controller.dto.MemberRoutine.MemberRoutineWorkoutContent;
import capstone.everyhealth.domain.routine.MemberRoutine;
import capstone.everyhealth.domain.routine.MemberRoutineContent;
import capstone.everyhealth.domain.routine.Workout;
import capstone.everyhealth.domain.routine.WorkoutName;
import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.service.MemberRoutineService;
import capstone.everyhealth.service.MemberService;
import capstone.everyhealth.service.WorkoutService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class MemberRoutineTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MemberRoutineController memberRoutineController;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRoutineService memberRoutineService;

    @Test
    @DisplayName("루틴 등록 테스트")
    @Transactional
    void routineRegisterIntegrationTest() throws Exception {

        // given
        ObjectMapper objectMapper = new ObjectMapper();

        String routineRegisterdate = String.valueOf(LocalDate.now());
        List<MemberRoutineWorkoutContent> memberRoutineWorkoutContentList = createMemberRoutineWorkoutContentList();
        MemberRoutineRegisterRequest memberRoutineRegisterRequest = createMemberRoutineRegisterRequest(routineRegisterdate, memberRoutineWorkoutContentList);

        Member member = createMember();
        Long memberId = memberService.saveMember(member);

        MemberRoutine memberRoutine = memberRoutineController.createMemberRoutine(memberId, memberRoutineRegisterRequest);
        memberRoutineController.createMemberRoutineContentList(memberRoutineRegisterRequest, memberRoutine);

        // when
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .post("/members/1/routines")
                        .content(objectMapper.registerModule(new JavaTimeModule()).writeValueAsString(memberRoutineRegisterRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Long savedRoutineId = Long.parseLong(mvcResult.getResponse().getContentAsString());
        MemberRoutine savedRoutine = memberRoutineService.findRoutineByRoutineId(savedRoutineId);

        // then
        assertEquals(memberRoutine.getMember().getId(), savedRoutine.getMember().getId());
        assertEquals(memberRoutine.getRoutineRegisterdate(), savedRoutine.getRoutineRegisterdate());
    }

    private List<MemberRoutineWorkoutContent> createMemberRoutineWorkoutContentList() {
        MemberRoutineWorkoutContent memberRoutineWorkoutContent1 = new MemberRoutineWorkoutContent();
        MemberRoutineWorkoutContent memberRoutineWorkoutContent2 = new MemberRoutineWorkoutContent();

        setMemberRoutineWorkoutContents(memberRoutineWorkoutContent1, memberRoutineWorkoutContent2);

        List<MemberRoutineWorkoutContent> memberRoutineWorkoutContentList = new ArrayList<>();

        memberRoutineWorkoutContentList.add(memberRoutineWorkoutContent1);
        memberRoutineWorkoutContentList.add(memberRoutineWorkoutContent2);

        return memberRoutineWorkoutContentList;
    }

    private Member createMember() {
        return Member.builder()
                .gymName("OO 헬스장")
                .weight(111)
                .height(199)
                .loginAt("kakao")
                .name("홍길동")
                .socialAccountId("123312")
                .build();
    }

    private MemberRoutineRegisterRequest createMemberRoutineRegisterRequest(String routineRegisterdate, List<MemberRoutineWorkoutContent> memberRoutineWorkoutContentList) {
        return MemberRoutineRegisterRequest.builder()
                .memberRoutineWorkoutContentList(memberRoutineWorkoutContentList)
                .routineRegisterdate(routineRegisterdate)
                .build();
    }

    private void setMemberRoutineWorkoutContents(MemberRoutineWorkoutContent memberRoutineWorkoutContent1, MemberRoutineWorkoutContent memberRoutineWorkoutContent2) {
        memberRoutineWorkoutContent1.setMemberRoutineWorkoutCount(20);
        memberRoutineWorkoutContent1.setMemberRoutineWorkoutName(WorkoutName.BENCH_PRESS_DUMBBELL);
        memberRoutineWorkoutContent1.setMemberRoutineWorkoutSet(10);
        memberRoutineWorkoutContent1.setMemberRoutineWorkoutTime(null);
        memberRoutineWorkoutContent1.setMemberRoutineWorkoutWeight(100);

        memberRoutineWorkoutContent2.setMemberRoutineWorkoutCount(null);
        memberRoutineWorkoutContent2.setMemberRoutineWorkoutName(WorkoutName.PLANK);
        memberRoutineWorkoutContent2.setMemberRoutineWorkoutSet(null);
        memberRoutineWorkoutContent2.setMemberRoutineWorkoutTime(100);
        memberRoutineWorkoutContent2.setMemberRoutineWorkoutWeight(null);
    }
}
