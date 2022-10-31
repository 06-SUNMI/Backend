package capstone.everyhealth.domain.routine;

import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
public class MemberRoutineContent implements Serializable{

    @Id
    @ManyToOne
    @JoinColumn(name = "routine_id")
    private MemberRoutine memberRoutine;

    private int sets;
    private int time;
    private boolean isChecked;
}
