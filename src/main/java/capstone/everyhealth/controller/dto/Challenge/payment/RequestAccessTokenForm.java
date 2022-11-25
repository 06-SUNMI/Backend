package capstone.everyhealth.controller.dto.Challenge.payment;

import lombok.Builder;
import lombok.Data;

@Data
public class RequestAccessTokenForm {
    private String imp_key;
    private String imp_secret;

    @Builder
    public RequestAccessTokenForm(String imp_key, String imp_secret){
        this.imp_key = imp_key;
        this.imp_secret = imp_secret;
    }
}
