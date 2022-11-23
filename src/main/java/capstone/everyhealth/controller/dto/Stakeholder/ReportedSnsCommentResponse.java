package capstone.everyhealth.controller.dto.Stakeholder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportedSnsCommentResponse {

    private Long reporterMemberId;
    private Long snsCommentId;
    private String reason;
}
