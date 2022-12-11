package capstone.everyhealth.controller.dto.Stakeholder;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeAuthPostReportPunishRequest {

    @ApiModelProperty(value = "제재 사유", example = "제재 사유")
    private String reason;

    @ApiModelProperty(value = "제재 기간", example = "3")
    private int blockDays;
}
