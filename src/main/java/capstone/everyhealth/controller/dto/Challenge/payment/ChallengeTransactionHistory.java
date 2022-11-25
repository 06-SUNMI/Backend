package capstone.everyhealth.controller.dto.Challenge.payment;

import capstone.everyhealth.domain.enums.ChallengePaymentStatus;
import capstone.everyhealth.domain.enums.ChallengeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeTransactionHistory {

    private String challengeName;
    private String challengeStartDate;
    private String challengeEndDate;
    private ChallengeStatus challengeStatus;
    private ChallengePaymentStatus challengePaymentStatus;
}
