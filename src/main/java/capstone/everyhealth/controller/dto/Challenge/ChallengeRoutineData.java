package capstone.everyhealth.controller.dto.Challenge;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeRoutineData {

    @ApiModelProperty(value = "루틴 진행 날짜",example = "2022-12-12")
    private String routineProgressDay;

    @ApiModelProperty(
            value = "루틴 상세 내용",
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
                    +"}"
                    + "]"
    )
    private List<ChallengeRoutineContentData> challengeRoutineContentDataList = new ArrayList<>();
}
