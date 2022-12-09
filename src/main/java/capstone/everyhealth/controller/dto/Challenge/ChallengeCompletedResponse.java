package capstone.everyhealth.controller.dto.Challenge;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeCompletedResponse {

    private Long challengeId;
    private String challengeName;
    private String finishedDate;
    private int totalParticipated;
    private int totalSuccessed;
    private int totalFailed;
    private int participateFee;
    private int individulReward;
    private boolean isCompleted;
}
