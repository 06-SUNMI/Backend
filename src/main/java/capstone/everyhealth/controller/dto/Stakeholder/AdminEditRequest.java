package capstone.everyhealth.controller.dto.Stakeholder;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminEditRequest {

    private String adminPassword;
    private String adminName;
    private String adminPhoneNumber;
}
