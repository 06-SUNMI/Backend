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

    @ApiModelProperty(value = "챌린지 주차",example = "1")
    private int ChallengeProgressWeek;

    private List<ChallengeRoutineContentData> challengeRoutineContentDataList = new ArrayList<>();
}
