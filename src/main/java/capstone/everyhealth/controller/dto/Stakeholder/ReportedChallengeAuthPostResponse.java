package capstone.everyhealth.controller.dto.Stakeholder;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportedChallengeAuthPostResponse {

    private Long reporterMemberId;
    private Long challengeAuthPostId;
    private String reason;
}
