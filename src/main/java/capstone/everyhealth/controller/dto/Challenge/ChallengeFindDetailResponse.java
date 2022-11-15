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
public class ChallengeFindDetailResponse {

    private Long challengeId;
    private String name;
    private String startDate;
    private String endDate;
    private int participationFee;
    private int participationNum;
    private int numPerWeek;
    private List<ChallengeRoutineData> challengeRoutineDataList = new ArrayList<>();
}
