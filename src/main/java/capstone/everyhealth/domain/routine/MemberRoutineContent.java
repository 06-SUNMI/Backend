package capstone.everyhealth.domain.routine;

import capstone.everyhealth.domain.challenge.ChallengeRoutine;
import capstone.everyhealth.domain.challenge.ChallengeRoutineContent;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRoutineContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "routine_id")
    private MemberRoutine memberRoutine;

    @OneToOne
    @JoinColumn(name = "challenge_routine_content_id")
    private ChallengeRoutineContent challengeRoutineContent;


    @OneToOne
    @JoinColumn(name = "workout_id")
    private Workout workout;

    private Integer memberRoutineWorkoutWeight;
    private Integer memberRoutineWorkoutCount;
    private Integer memberRoutineWorkoutSet;
    private Integer memberRoutineWorkoutTime;
    @Builder.Default
    private boolean memberRoutineIsChecked = false;

    public void changeMemberRoutine(MemberRoutine memberRoutine) {
        this.memberRoutine = memberRoutine;
    }

    public void addMemberRoutine(MemberRoutine memberRoutine){
        this.memberRoutine = memberRoutine;
        this.memberRoutine.getMemberRoutineContentList().add(this);
    }
}
