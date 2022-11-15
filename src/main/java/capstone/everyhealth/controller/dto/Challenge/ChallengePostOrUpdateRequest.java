package capstone.everyhealth.controller.dto.Challenge;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengePostOrUpdateRequest {

    @ApiModelProperty(value = "챌린지 이름", example = "OO 챌린지")
    private String challengeName;

    @ApiModelProperty(value = "챌린지 시작일", example = "2022-11-06")
    private String challengeStartDate;

    @ApiModelProperty(value = "챌린지 종료일", example = "2022-11-19")
    private String challengeEndDate;

    @ApiModelProperty(value = "챌린지 참가비", example = "30000")
    private int challengeParticipationFee;

    @ApiModelProperty(value = "주 당 챌린지 참여 횟수", example = "2")
    private int challengeNumPerWeek;

    @ApiModelProperty(
            value = "챌린지 루틴 목록 (총 2주, 주 2회 예시)",
            example = "["
                    + "{"
                    + "\"challengeProgressWeek\": 1,"
                    + "\"challengeRoutineContentDataList\":"
                    + "["
                    + "{"
                    + "\"challengeRoutineWorkoutName\": \"PLANK\","
                    + "\"challengeRoutineWorkoutWeight\": null,"
                    + "\"challengeRoutineWorkoutCount\": null,"
                    + "\"challengeRoutineWorkoutSet\": null,"
                    + "\"challengeRoutineWorkoutTime\": 120"
                    + "},"
                    + "{"
                    + "\"challengeRoutineWorkoutName\":\"CHEST_PRESS_MACHINE\","
                    + "\"challengeRoutineWorkoutWeight\":100,"
                    + "\"challengeRoutineWorkoutCount\":10,"
                    + "\"challengeRoutineWorkoutSet\":2,"
                    + "\"challengeRoutineWorkoutTime\":null"
                    + "}"
                    + "]"
                    + "},"
                    + "{"
                    + "\"challengeProgressWeek\": 1,"
                    + "\"challengeRoutineContentDataList\":"
                    + "["
                    + "{"
                    + "\"challengeRoutineWorkoutName\": \"PUSH_UP\","
                    + "\"challengeRoutineWorkoutWeight\": null,"
                    + "\"challengeRoutineWorkoutCount\": 20,"
                    + "\"challengeRoutineWorkoutSet\": 10,"
                    + "\"challengeRoutineWorkoutTime\": null"
                    + "},"
                    + "{"
                    + "\"challengeRoutineWorkoutName\":\"CHEST_PRESS_MACHINE\","
                    + "\"challengeRoutineWorkoutWeight\":100,"
                    + "\"challengeRoutineWorkoutCount\":10,"
                    + "\"challengeRoutineWorkoutSet\":2,"
                    + "\"challengeRoutineWorkoutTime\":null"
                    + "}"
                    + "]"
                    + "},"
                    + "{"
                    + "\"challengeProgressWeek\": 2,"
                    + "\"challengeRoutineContentDataList\":"
                    + "["
                    + "{"
                    + "\"challengeRoutineWorkoutName\": \"PUSH_UP\","
                    + "\"challengeRoutineWorkoutWeight\": null,"
                    + "\"challengeRoutineWorkoutCount\": 30,"
                    + "\"challengeRoutineWorkoutSet\": 20,"
                    + "\"challengeRoutineWorkoutTime\": null"
                    + "},"
                    + "{"
                    + "\"challengeRoutineWorkoutName\":\"CHEST_PRESS_MACHINE\","
                    + "\"challengeRoutineWorkoutWeight\":120,"
                    + "\"challengeRoutineWorkoutCount\":8,"
                    + "\"challengeRoutineWorkoutSet\":4,"
                    + "\"challengeRoutineWorkoutTime\":null"
                    + "}"
                    + "]"
                    + "},"
                    + "{"
                    + "\"challengeProgressWeek\": 2,"
                    + "\"challengeRoutineContentDataList\":"
                    + "["
                    + "{"
                    + "\"challengeRoutineWorkoutName\": \"PUSH_UP\","
                    + "\"challengeRoutineWorkoutWeight\": null,"
                    + "\"challengeRoutineWorkoutCount\": 20,"
                    + "\"challengeRoutineWorkoutSet\": 10,"
                    + "\"challengeRoutineWorkoutTime\": null"
                    + "},"
                    + "{"
                    + "\"challengeRoutineWorkoutName\":\"PLANK\","
                    + "\"challengeRoutineWorkoutWeight\":null,"
                    + "\"challengeRoutineWorkoutCount\":null,"
                    + "\"challengeRoutineWorkoutSet\":null,"
                    + "\"challengeRoutineWorkoutTime\":120"
                    + "}"
                    + "]"
                    + "}"
                    + "]"
    )
    private List<ChallengeRoutineData> challengeRoutineDataList = new ArrayList<>();
}
