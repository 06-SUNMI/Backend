package capstone.everyhealth.domain.challenge;

import capstone.everyhealth.domain.challenge.ChallengeRoutine;
import capstone.everyhealth.domain.routine.Workout;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeRoutineContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "routine_id")
    private ChallengeRoutine challengeRoutine;

    @OneToOne
    @JoinColumn(name = "workout_id")
    private Workout workout;

    private int progressWeek;
    private String progressDayOfWeek;

    private Integer challengeRoutineWorkoutWeight;
    private Integer challengeRoutineWorkoutCount;
    private Integer challengeRoutineWorkoutSet;
    private Integer challengeRoutineWorkoutTime;

    public void changeChallengeRoutine(ChallengeRoutine challengeRoutine) {
        this.challengeRoutine = challengeRoutine;
    }

    public void addChallengeRoutine(ChallengeRoutine challengeRoutine){
        this.challengeRoutine = challengeRoutine;
        this.challengeRoutine.getChallengeRoutineContentList().add(this);
    }
}
