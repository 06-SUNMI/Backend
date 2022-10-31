package capstone.everyhealth.domain.routine;

import lombok.Getter;
import capstone.everyhealth.domain.enums.WorkoutTargetEnum;

import javax.persistence.*;

@Entity
@Getter
public class WorkoutTarget {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private WorkoutTargetEnum target;
}