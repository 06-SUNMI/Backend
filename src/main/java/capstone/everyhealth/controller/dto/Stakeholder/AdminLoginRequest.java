package capstone.everyhealth.controller.dto.Stakeholder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginRequest {

    @NotBlank(message = "아이디를 입력해 주세요.")
    private String adminId;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String adminPassword;
}
