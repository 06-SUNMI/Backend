package capstone.everyhealth.domain.challenge;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
public class Challenge {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int participationFee;
    private int participationNum;
    private String preparations;
    private int numPerWeek;
}
