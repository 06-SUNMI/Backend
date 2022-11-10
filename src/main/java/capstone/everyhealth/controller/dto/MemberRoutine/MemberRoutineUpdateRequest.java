package capstone.everyhealth.controller.dto.MemberRoutine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MemberRoutineUpdateRequest {

    @ApiModelProperty(
            name = "수정 후 루틴 내 운동 리스트",
            value = "수정 후 루틴 내 운동 리스트",
            example = "[ "
                    + "{"
                    + "\"memberRoutineWorkoutName\" : \"PLANK\","
                    + "\"memberRoutineWorkoutWeight\" : null,"
                    + "\"memberRoutineWorkoutCount\" : null,"
                    + "\"memberRoutineWorkoutSet\" : null,"
                    + "\"memberRoutineWorkoutTime\" : 200"
                    + "},"
                    + "{"
                    + "\"memberRoutineWorkoutName\" : \"BICEP_CURL_DUMBBELL\","
                    + "\"memberRoutineWorkoutWeight\" : 60,"
                    + "\"memberRoutineWorkoutCount\" : 20,"
                    + "\"memberRoutineWorkoutSet\" : 4,"
                    + "\"memberRoutineWorkoutTime\" : null"
                    + "},"
                    + "{"
                    + "\"memberRoutineWorkoutName\" : \"ONE_ARM_TRICEP_EXTENSION_DUMBBELL\","
                    + "\"memberRoutineWorkoutWeight\" : 100,"
                    + "\"memberRoutineWorkoutCount\" : 10,"
                    + "\"memberRoutineWorkoutSet\" : 3,"
                    + "\"memberRoutineWorkoutTime\" : null"
                    + "}"
                    + "]"
    )
    private List<MemberRoutineContentData> memberRoutineContentList = new ArrayList<>();
}

