package capstone.everyhealth.controller.dto.Stakeholder;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberFindResponse {

    @ApiModelProperty(value="소셜 로그인한 곳",example = "카카오")
    private String loginAt;
    @ApiModelProperty(value="소셜 로그인한 곳 고유 id",example = "12313131")
    private String socialId;
}
