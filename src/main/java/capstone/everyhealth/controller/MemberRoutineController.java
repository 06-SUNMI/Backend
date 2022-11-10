package capstone.everyhealth.controller;

import capstone.everyhealth.controller.dto.MemberRoutine.*;
import capstone.everyhealth.domain.routine.*;
import capstone.everyhealth.service.MemberRoutineService;
import capstone.everyhealth.service.MemberService;
import capstone.everyhealth.service.WorkoutService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"멤버 루틴 API"})
public class MemberRoutineController {
    private final MemberRoutineService routineService;
    private final WorkoutService workoutService;
    private final MemberService memberService;

    @ApiOperation(
            value = "루틴 등록하기",
            notes = "사용자가 만든 루틴을 등록한다.\n"
                    + "사용자의 id(ex) 15.xxx.xxx.xxx:8080/members/1/routines)와 루틴 등록 날짜와 루틴 상세 내용을 보내면 루틴 저장이 완료된다.\n"
                    + "저장된 루틴의 id 값을 반환하여 이를 통해 해당 루틴의 상세 정보를 확인할 수 있다.\n"
    )
    @PostMapping("/members/{memberId}/routines")
    public Long registerMemberRoutine(@ApiParam(value = "사용자의 id값", example = "1") @PathVariable Long memberId, @ApiParam(value = "루틴에 추가한 운동 정보 목록과 등록 날짜") @RequestBody MemberRoutineRegisterRequest memberRoutineRegisterRequest) {

        MemberRoutine memberRoutine = createMemberRoutine(memberId, memberRoutineRegisterRequest);
        createMemberRoutineContentList(memberRoutineRegisterRequest, memberRoutine);

        return routineService.save(memberRoutine);
    }

    @ApiOperation(
            value = "루틴 전체 조회하기",
            notes = "특정 사용자가 등록한 모든 루틴을 조회한다."
                    + "\n사용자의 id를 넣고 실행하면 해당 id를 가지는 사용자의 전체 루틴 정보 리스트를 반환한다."
                    + "\n루틴 정보서 routineId를 이용하여 해당 루틴에 대한 상세 정보 조회가 가능하다."
                    + "\nroutineRegisterDate는 화면 표시에 필요한 정보이다."
    )
    @ResponseBody
    @GetMapping("/members/{memberId}/routines")
    public MemberRoutineFindAllResponse findAllRoutines(@ApiParam(/*name = "member_id",*/ value = "사용자의 id값", example = "1") @PathVariable Long memberId) {

        List<MemberRoutine> memberRoutineList = routineService.findAllRoutines(memberId);

        return new MemberRoutineFindAllResponse(memberRoutineList);
    }

    @ApiOperation(
            value = "특정 루틴 상세 조회하기",
            notes = "사용자가 특정 루틴을 누르면 그 루틴에 대한 상세 정보를 전달한다."
    )
    @ResponseBody
    @GetMapping("/routines/{routineId}")
    public MemberRoutineFindByRoutineId findRoutineByRoutineId(@ApiParam(value = "루틴의 id값", example = "1") @PathVariable Long routineId) {

        MemberRoutine memberRoutine = routineService.findRoutineByRoutineId(routineId);
        MemberRoutineFindByRoutineId memberRoutineFindByRoutineId = new MemberRoutineFindByRoutineId(memberRoutine);

        return memberRoutineFindByRoutineId;
    }

    @ApiOperation(
            value = "루틴 수정하기",
            notes = "사용자가 등록한 특정 루틴을 수정한다."
    )
    @ResponseBody
    @PutMapping("/routines/{routineId}")
    public void update(@ApiParam(value = "루틴의 id값", example = "1") @PathVariable Long routineId, @ApiParam(value = "수정 후 운동 목록") @RequestBody MemberRoutineUpdateRequest memberRoutineUpdateRequest) {

        List<MemberRoutineContent> memberRoutineContentList = new ArrayList<>();

        addMemberRoutineContent(memberRoutineUpdateRequest, memberRoutineContentList);
        routineService.updateRoutine(routineId, memberRoutineContentList);
    }

    @ApiOperation(
            value = "루틴 삭제하기",
            notes = "사용자가 등록한 특정 루틴을 삭제한다."
    )
    @ResponseBody
    @DeleteMapping("/routines/{routineId}")
    public void delete(@ApiParam(value = "루틴의 id값", example = "1") @PathVariable Long routineId) {

        routineService.deleteRoutine(routineId);
    }

    public MemberRoutine createMemberRoutine(Long memberId, MemberRoutineRegisterRequest memberRoutineRegisterRequest) {

        return MemberRoutine.builder()
                .member(memberService.findMemberById(memberId))
                .memberRoutineContentList(new ArrayList<>())
                .routineRegisterdate(memberRoutineRegisterRequest.getRoutineRegisterdate())
                .build();
    }

    public void createMemberRoutineContentList(MemberRoutineRegisterRequest memberRoutineRegisterRequest, MemberRoutine memberRoutine) {

        for (MemberRoutineWorkoutContent memberRoutineWorkoutContent : memberRoutineRegisterRequest.getMemberRoutineWorkoutContentList()) {

            WorkoutName workoutName = memberRoutineWorkoutContent.getMemberRoutineWorkoutName();

            Workout workout = workoutService.findByWorkoutName(workoutName);
            MemberRoutineContent memberRoutineContent = createMemberRoutineContent(memberRoutineWorkoutContent, workout);

            memberRoutine.addMemberRoutineContent(memberRoutineContent);
        }
    }

    public MemberRoutineContent createMemberRoutineContent(MemberRoutineWorkoutContent memberRoutineWorkoutContent, Workout workout) {
        return MemberRoutineContent.builder()
                .workout(workout)
                .memberRoutineWorkoutCount(memberRoutineWorkoutContent.getMemberRoutineWorkoutCount())
                .memberRoutineWorkoutSet(memberRoutineWorkoutContent.getMemberRoutineWorkoutSet())
                .memberRoutineWorkoutTime(memberRoutineWorkoutContent.getMemberRoutineWorkoutTime())
                .memberRoutineWorkoutWeight(memberRoutineWorkoutContent.getMemberRoutineWorkoutWeight())
                .build();
    }

    private void addMemberRoutineContent(MemberRoutineUpdateRequest memberRoutineUpdateReqeust, List<MemberRoutineContent> memberRoutineContentList) {
        for (MemberRoutineContentData memberRoutineContentData : memberRoutineUpdateReqeust.getMemberRoutineContentList()) {

            WorkoutName workoutName = memberRoutineContentData.getMemberRoutineWorkoutName();
            Workout workout = workoutService.findByWorkoutName(workoutName);

            MemberRoutineContent memberRoutineContent = MemberRoutineContent.builder()
                    .memberRoutineWorkoutWeight(memberRoutineContentData.getMemberRoutineWorkoutWeight())
                    .memberRoutineWorkoutTime(memberRoutineContentData.getMemberRoutineWorkoutTime())
                    .memberRoutineWorkoutCount(memberRoutineContentData.getMemberRoutineWorkoutCount())
                    .memberRoutineWorkoutSet(memberRoutineContentData.getMemberRoutineWorkoutSet())
                    .workout(workout)
                    .build();

            memberRoutineContentList.add(memberRoutineContent);
        }
    }
}
