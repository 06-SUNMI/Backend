package capstone.everyhealth.controller.dto.Challenge.payment;

import lombok.Data;

@Data
public class PaymentResultResponseForm {

    private String imp_uid;
    private String merchant_uid;
    private Long challenge_id;
    private Long member_id;
}
