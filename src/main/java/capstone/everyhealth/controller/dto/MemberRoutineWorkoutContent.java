package capstone.everyhealth.controller.dto;

import capstone.everyhealth.domain.routine.WorkoutName;
import lombok.Data;

@Data
public class MemberRoutineWorkoutContent {

    private WorkoutName memberRoutineWorkoutName;
    private Integer memberRoutineWorkoutWeight;
    private Integer memberRoutineWorkoutCount;
    private Integer memberRoutineWorkoutSet;
    private Integer memberRoutineWorkoutTime;
}
