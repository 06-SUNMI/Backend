package capstone.everyhealth.controller.dto.Stakeholder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportedSnsPostResponse {

    private Long reporterMemberId;
    private Long snsPostId;
    private String reason;
}
