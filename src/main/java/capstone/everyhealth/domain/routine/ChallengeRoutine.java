package capstone.everyhealth.domain.routine;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
public class ChallengeRoutine {

    @Id @GeneratedValue
    private Long id;

    private LocalDateTime date;
}
