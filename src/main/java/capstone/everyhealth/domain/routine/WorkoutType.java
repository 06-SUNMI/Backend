package capstone.everyhealth.domain.routine;

import lombok.Getter;
import capstone.everyhealth.domain.enums.WorkoutTypeEnum;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
public class WorkoutType implements Serializable {

    @Id @GeneratedValue
    private Long id;

    @Id
    @ManyToOne
    @JoinColumn(name = "workout_target_id")
    private WorkoutTarget target;

    private WorkoutTypeEnum type;

    // 타입 m <ㅡ> 1 타겟
}
