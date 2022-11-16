package capstone.everyhealth.controller.dto.Challenge;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeRoutineCopyToParticipantData {

    private Long challengeRoutineId;
    private String challengeRoutineProgressDate;
}
