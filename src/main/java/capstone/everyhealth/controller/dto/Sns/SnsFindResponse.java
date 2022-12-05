package capstone.everyhealth.controller.dto.Sns;

import capstone.everyhealth.domain.stakeholder.Member;
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
public class SnsFindResponse {

    private Long snsPostId;
    private Long memberId;
    private List<String> snsImageOrVideoLinkList = new ArrayList<>();
    private String snsContent;
    private int snsLikesNum;
    private String memberName;
    private String memberProfilePhotoUrl;
}
