package capstone.everyhealth.controller;

import capstone.everyhealth.domain.routine.MemberRoutine;
import capstone.everyhealth.domain.routine.MemberRoutineContent;
import capstone.everyhealth.domain.routine.Workout;
import capstone.everyhealth.service.RoutineService;
import capstone.everyhealth.service.StakeholderService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RoutineController {
    private final RoutineService routineService;
    private final StakeholderService stakeholderService;

    @ApiOperation(
            value="루틴 등록하기",
            notes="사용자가 만든 루틴을 등록한다."
    )
    @ResponseBody
    @PostMapping("/routines/{memberId}")
    public Long registerMemberRoutine(@PathVariable Long memberId, @RequestBody MemberRoutineRegisterDto dto) {

        List<MemberRoutineContent> contentList = new ArrayList<>();

        addContentToContentList(dto, contentList);
        MemberRoutine routine = getMemberRoutine(memberId, dto, contentList);

        return routineService.save(routine);
    }

    private void addContentToContentList(MemberRoutineRegisterDto dto, List<MemberRoutineContent> contentList) {

        for (int idx = 0; idx < dto.getCountList().size(); idx++) {

            Workout workout = Workout.builder()
                    .target(dto.getTargetList().get(idx))
                    .type(dto.getTypeList().get(idx))
                    .build();

            MemberRoutineContent content = MemberRoutineContent.builder()
                    .workout(workout)
                    .weight(dto.getWeightList().get(idx))
                    .count(dto.getCountList().get(idx))
                    .set(dto.getSetList().get(idx))
                    .time(dto.getTimeList().get(idx))
                    .build();

            contentList.add(content);
        }
    }

    private MemberRoutine getMemberRoutine(Long memberId, MemberRoutineRegisterDto dto, List<MemberRoutineContent> contentList) {
        return MemberRoutine.builder()
                .member(stakeholderService.findById(memberId).get())
                .routineContentList(contentList)
                .date(dto.getDate())
                .build();
    }
}
