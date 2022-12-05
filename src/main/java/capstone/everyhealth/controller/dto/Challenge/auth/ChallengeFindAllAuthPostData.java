package capstone.everyhealth.controller.dto.Challenge.auth;

import capstone.everyhealth.domain.challenge.Challenge;
import capstone.everyhealth.domain.stakeholder.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeFindAllAuthPostData {

    private Long challengeAuthPostId;
    private Long memberId;
    private String photoUrl;
    private int reportedNum;
}
