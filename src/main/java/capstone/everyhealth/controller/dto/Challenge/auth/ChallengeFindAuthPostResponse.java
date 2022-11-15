package capstone.everyhealth.controller.dto.Challenge.auth;

import capstone.everyhealth.domain.challenge.ChallengeRoutine;
import capstone.everyhealth.domain.stakeholder.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeFindAuthPostResponse {

    private String photoUrl;
    private int reportedNum;
}
