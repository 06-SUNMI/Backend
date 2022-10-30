package capstone.everyhealth.domain.routine;

import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
public class UserRoutineContent implements Serializable{

    @Id
    @ManyToOne
    @JoinColumn(name = "routine_id")
    private UserRoutine userRoutine;

    private int set;
    private int time;
    //@ColumnDefault(value = "false")
    // default로 boolean 설정 방법 찾기
    private boolean isChecked;
}
