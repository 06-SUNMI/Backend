package capstone.everyhealth.domain.challenge;

import capstone.everyhealth.domain.enums.ChallengePaymentStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "challenge_participant_id")
    private ChallengeParticipant challengeParticipant;

    @Enumerated(EnumType.STRING)
    private ChallengePaymentStatus challengePaymentStatus;

    private String merchantUid;

    public void addTransaction(ChallengeParticipant challengeParticipant){
        this.challengeParticipant=challengeParticipant;
        challengeParticipant.setChallengeTransaction(this);
    }
}
