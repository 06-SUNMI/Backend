package capstone.everyhealth.domain.challenge;

import capstone.everyhealth.controller.dto.Challenge.ChallengePostOrUpdateRequest;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeRoutine> challengeRoutineList = new ArrayList<>();

    private String name;
    private String startDate;
    private String endDate;
    private int participationFee;
    private int participationNum;
    private int numPerWeek;
    @Builder.Default
    private boolean isFinished = false;
}
