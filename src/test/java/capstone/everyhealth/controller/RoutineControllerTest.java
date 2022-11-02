package capstone.everyhealth.controller;

import capstone.everyhealth.domain.routine.MemberRoutine;
import capstone.everyhealth.domain.routine.MemberRoutineContent;
import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.repository.RoutineRepository;
import capstone.everyhealth.service.RoutineService;
import capstone.everyhealth.service.StakeholderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(controllers = RoutineController.class)
@Slf4j
class RoutineControllerTest {

    @InjectMocks
    private RoutineController routineController;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RoutineService routineService;

    @MockBean
    private StakeholderService stakeholderService;

    @Test
    @DisplayName("루틴 등록 컨트롤러 단위 테스트")
    void routineRegisterTest() throws Exception {

        log.info("mvc = {}",mvc);

        // given
        ObjectMapper objectMapper = new ObjectMapper();
        MemberRoutineRegisterDto registerDto = getRegisterDto();

        when(routineService.save(any(MemberRoutine.class))).thenReturn(1L);
        when(stakeholderService.findById(any(Long.class))).thenReturn(Optional.ofNullable(Member.builder().id(1L).build()));

        // when & then
        mvc.perform(MockMvcRequestBuilders
                        .post("/routines/1")
                        .content(objectMapper.writeValueAsString(registerDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(Long.toString(1L)));

        verify(routineService).save(any(MemberRoutine.class));
        verify(stakeholderService).findById(any(Long.class));
    }

    private MemberRoutineRegisterDto getRegisterDto() {
        return MemberRoutineRegisterDto.builder()
                .typeList(new ArrayList<>(Arrays.asList("팔운동1", "팔운동2", "다리운동1")))
                .targetList(new ArrayList<>(Arrays.asList("팔", "팔", "다리")))
                .countList(new ArrayList<>(Arrays.asList(20, 30, 40)))
                .weightList(new ArrayList<>(Arrays.asList(50, 60, 70)))
                .setList(new ArrayList<>(Arrays.asList(20, 10, 30)))
                .timeList(new ArrayList<>(Arrays.asList(60, 120, 90)))
                .date("2022-03-21")
                .build();
    }
}
