package capstone.everyhealth.controller.dto.Sns;

import capstone.everyhealth.domain.stakeholder.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class SnsFindResponse {
    private Long memberId;
    private String snsImageLink;
    private String snsVideoLink;
    private String snsContent;

}
