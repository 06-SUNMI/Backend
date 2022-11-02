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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "routine_id")
    private MemberRoutine memberRoutine;

    @OneToOne
    @JoinColumn(name = "workout_id")
    private Workout workout;

    private Integer weight;
    private Integer numCount;
    private Integer numSet;
    private Integer numTime;
    private boolean isChecked;
}
