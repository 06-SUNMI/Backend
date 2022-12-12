package capstone.everyhealth.controller.dto.Stakeholder;

import capstone.everyhealth.domain.stakeholder.Admin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AdminFindResponse {

    private String adminId;
    private String adminPassword;
    private String adminName;
    private String adminPhoneNumber;

    public AdminFindResponse(Admin admin){

        this.adminId = admin.getAdminId();
        this.adminPassword = admin.getAdminPassword();
        this.adminName = admin.getAdminName();
    }
}
