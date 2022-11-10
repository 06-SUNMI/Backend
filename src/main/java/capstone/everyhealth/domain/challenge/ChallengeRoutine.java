package capstone.everyhealth.domain.challenge;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeRoutine {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="challenge_id")
    private Challenge challenge;

    @OneToMany(mappedBy = "challengeRoutine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeRoutineContent> challengeRoutineContentList = new ArrayList<>();

    private LocalDateTime date;

    public void addChallenge(Challenge challenge){
        this.challenge = challenge;
        this.challenge.getChallengeRoutineList().add(this);
    }
}
