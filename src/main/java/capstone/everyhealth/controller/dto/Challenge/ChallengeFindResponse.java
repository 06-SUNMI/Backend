package capstone.everyhealth.controller.dto.Challenge;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeFindResponse {

    private Long challengeId;
    private String name;
    private String startDate;
    private String endDate;
    private int participationFee;
    private int participationNum;
    private String preparations;
    private int numPerWeek;
}
