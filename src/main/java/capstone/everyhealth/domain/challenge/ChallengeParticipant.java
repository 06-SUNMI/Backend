package capstone.everyhealth.domain.challenge;

import lombok.Getter;
import capstone.everyhealth.domain.stakeholder.Member;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
public class ChallengeParticipant implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @Id
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
