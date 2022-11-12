package capstone.everyhealth.domain.challenge;

import capstone.everyhealth.domain.challenge.ChallengeRoutine;
import capstone.everyhealth.domain.routine.Workout;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
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

    private Integer challengeRoutineWorkoutWeight;
    private Integer challengeRoutineWorkoutCount;
    private Integer challengeRoutineWorkoutSet;
    private Integer challengeRoutineWorkoutTime;

    public void changeChallengeRoutine(ChallengeRoutine challengeRoutine) {
        this.challengeRoutine = challengeRoutine;
    }

    public void addChallengeRoutine(ChallengeRoutine challengeRoutine){
        log.info("aaaa");
        this.challengeRoutine = challengeRoutine;
        log.info("bbbb");
        this.challengeRoutine.getChallengeRoutineContentList().add(this);
        log.info("cccc");
    }
}
