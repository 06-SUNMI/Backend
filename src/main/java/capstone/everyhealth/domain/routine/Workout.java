package capstone.everyhealth.domain.routine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Workout {

    @Id
    @GeneratedValue
    private Long id;

    private String type;
    private String target;
}
