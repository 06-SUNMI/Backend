package capstone.everyhealth.domain.routine;

import lombok.Getter;
import capstone.everyhealth.domain.stakeholder.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class UserRoutine {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime date;
}
