package capstone.everyhealth.domain.challenge;

import lombok.Getter;
import capstone.everyhealth.domain.stakeholder.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    @JoinColumn(name = "user_id")
    private User user;
}
