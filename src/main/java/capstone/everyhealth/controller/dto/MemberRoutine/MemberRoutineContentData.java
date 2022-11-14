package capstone.everyhealth.controller.dto.MemberRoutine;

import capstone.everyhealth.domain.routine.WorkoutName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRoutineContentData {

    @ApiModelProperty(value = "등록 운동 id 값",example = "1")
    private Long memberRoutineContentId;
    @ApiModelProperty(value = "운동 이름",example = "PUSH_UP")
    private WorkoutName memberRoutineWorkoutName;
    @ApiModelProperty(value = "운동 중량",example = "50")
    private Integer memberRoutineWorkoutWeight;
    @ApiModelProperty(value = "운동 횟수",example = "30")
    private Integer memberRoutineWorkoutCount;
    @ApiModelProperty(value = "운동 세트수",example = "3")
    private Integer memberRoutineWorkoutSet;
    @ApiModelProperty(value = "운동 시간(분으로? 초로? 정하기)",example = "100")
    private Integer memberRoutineWorkoutTime;
}
