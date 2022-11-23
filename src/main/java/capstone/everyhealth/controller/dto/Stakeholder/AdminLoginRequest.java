package capstone.everyhealth.controller.dto.Stakeholder;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AdminLoginRequest {

    @ApiModelProperty(value = "관리자 id",example = "admin")
    private String adminId;
    @ApiModelProperty(value = "관리자 pw",example = "1234")
    private String adminPassword;
}
