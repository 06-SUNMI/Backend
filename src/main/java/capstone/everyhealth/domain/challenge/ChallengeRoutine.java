package capstone.everyhealth.domain.challenge;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeRoutine {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="challenge_id")
    private Challenge challenge;

    @Builder.Default
    @OneToMany(mappedBy = "challengeRoutine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeRoutineContent> challengeRoutineContentList = new ArrayList<>();

    private int progressWeek;

    public void addChallenge(Challenge challenge){
        this.challenge = challenge;
        this.challenge.getChallengeRoutineList().add(this);
    }
}
