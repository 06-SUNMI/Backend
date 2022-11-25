package capstone.everyhealth.controller.dto.Challenge.payment;

import lombok.Data;

@Data
public class ResponseAccessTokenForm {
    private String code;
    private String message;
    private String response;
}
