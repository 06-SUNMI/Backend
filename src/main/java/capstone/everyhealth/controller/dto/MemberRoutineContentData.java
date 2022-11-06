package capstone.everyhealth.controller.dto;

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
