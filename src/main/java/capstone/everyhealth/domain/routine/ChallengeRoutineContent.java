package capstone.everyhealth.domain.routine;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
@Getter
public class ChallengeRoutineContent implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "routine_id")
    private ChallengeRoutine challengeRoutine;

    private int set;
    private int time;
}
