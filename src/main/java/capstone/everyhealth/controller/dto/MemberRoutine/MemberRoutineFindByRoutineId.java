package capstone.everyhealth.controller.dto.MemberRoutine;

import capstone.everyhealth.domain.routine.MemberRoutine;
import capstone.everyhealth.domain.routine.MemberRoutineContent;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class MemberRoutineFindByRoutineId {

    @ApiModelProperty(
            value = "루틴에 등록한 운동들 상세 내용",
            example = "["
                    + "{"
                    + "\"memberRoutineWorkoutName\": \"PUSH_UP\","
                    + "\"memberRoutineWorkoutWeight\": null,"
                    + "\"memberRoutineWorkoutCount\": 20,"
                    + "\"memberRoutineWorkoutSet\": 10,"
                    + "\"memberRoutineWorkoutTime\": null"
                    + "},"
                    + "{"
                    + "\"memberRoutineWorkoutName\":\"CHEST_PRESS_MACHINE\","
                    + "\"memberRoutineWorkoutWeight\":100,"
                    + "\"memberRoutineWorkoutCount\":10,"
                    + "\"memberRoutineWorkoutSet\":2,"
                    + "\"memberRoutineWorkoutTime\":null"
                    + "}"
                    + "]"
    )
    private MemberRoutineData memberRoutineData;

    public MemberRoutineFindByRoutineId(MemberRoutine memberRoutine) {

        MemberRoutineData memberRoutineData = new MemberRoutineData();

        for (MemberRoutineContent memberRoutineContent : memberRoutine.getMemberRoutineContentList()) {

            MemberRoutineContentData memberRoutineContentData = createMemberRoutineContentData(memberRoutineContent);
            memberRoutineData.getMemberRoutineContentList().add(memberRoutineContentData);
        }

        this.memberRoutineData = memberRoutineData;
    }

    private MemberRoutineContentData createMemberRoutineContentData(MemberRoutineContent memberRoutineContent) {
        return MemberRoutineContentData.builder()
                .memberRoutineContentId(memberRoutineContent.getId())
                .memberRoutineWorkoutCount(memberRoutineContent.getMemberRoutineWorkoutCount())
                .memberRoutineWorkoutSet(memberRoutineContent.getMemberRoutineWorkoutSet())
                .memberRoutineWorkoutTime(memberRoutineContent.getMemberRoutineWorkoutTime())
                .memberRoutineWorkoutWeight(memberRoutineContent.getMemberRoutineWorkoutWeight())
                .memberRoutineWorkoutName(memberRoutineContent.getWorkout().getWorkoutName())
                .build();
    }

    @Data
    @NoArgsConstructor
    class MemberRoutineData {
        @ApiModelProperty(
                value = "루틴에 등록한 운동들 상세 내용",
                example = "["
                        + "{"
                        + "\"memberRoutineContentId\": 1,"
                        + "\"memberRoutineWorkoutName\": \"PUSH_UP\","
                        + "\"memberRoutineWorkoutWeight\": null,"
                        + "\"memberRoutineWorkoutCount\": 20,"
                        + "\"memberRoutineWorkoutSet\": 10,"
                        + "\"memberRoutineWorkoutTime\": null"
                        + "},"
                        + "{"
                        + "\"memberRoutineContentId\": 2,"
                        + "\"memberRoutineWorkoutName\":\"CHEST_PRESS_MACHINE\","
                        + "\"memberRoutineWorkoutWeight\":100,"
                        + "\"memberRoutineWorkoutCount\":10,"
                        + "\"memberRoutineWorkoutSet\":2,"
                        + "\"memberRoutineWorkoutTime\":null"
                        + "}"
                        + "]"
        )
        private List<MemberRoutineContentData> memberRoutineContentList = new ArrayList<>();
    }
}
