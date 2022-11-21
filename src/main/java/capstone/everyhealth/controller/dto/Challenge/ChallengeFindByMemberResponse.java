package capstone.everyhealth.controller.dto.Challenge;

import capstone.everyhealth.domain.enums.ChallengeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeFindByMemberResponse {

    private Long challengeId;
    private String name;
    private String startDate;
    private String endDate;
    private int participationFee;
    private int participationNum;
    private int numPerWeek;
    private int progressRate;
    private ChallengeStatus challengeParticipantStatus;
}
