package capstone.everyhealth.domain.routine;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRoutineContent {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "routine_id")
    private MemberRoutine memberRoutine;

    @OneToOne
    @JoinColumn(name = "workout_id")
    private Workout workout;

    private Integer weight;
    private Integer count;
    private Integer set;
    private Integer time;
    private boolean isChecked;
}
