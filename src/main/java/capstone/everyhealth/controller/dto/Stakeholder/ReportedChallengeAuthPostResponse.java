package capstone.everyhealth.controller.dto.Stakeholder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportedChallengeAuthPostResponse {

    private Long reporterMemberId;
    private Long challengeAuthPostId;
    private String reason;
}
