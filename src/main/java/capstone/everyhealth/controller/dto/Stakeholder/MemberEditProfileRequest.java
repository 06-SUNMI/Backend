package capstone.everyhealth.controller.dto.Stakeholder;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberEditProfileRequest {

    @ApiModelProperty(value = "유저 이름",example = "홍길동")
    private String memberName;
    @ApiModelProperty(value = "유저 키",example = "178")
    private int memberHeight;
    @ApiModelProperty(value = "유저 몸무게",example = "66")
    private int memberWeight;
    @ApiModelProperty(value = "유저가 등록한 헬스장",example = "OO 헬스장")
    private String memberRegisteredGymName;
    @ApiModelProperty(value = "유저가 등록한 헬스장 id",example = "1124152")
    private String memberRegisteredGymId;
}
