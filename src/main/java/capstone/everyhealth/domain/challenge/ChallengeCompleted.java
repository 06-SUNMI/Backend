package capstone.everyhealth.domain.challenge;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeCompleted {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    private int succeededParticipantNum;
    private int individualReward;
    private boolean isCompleted;
}
