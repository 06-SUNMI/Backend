package capstone.everyhealth.controller.dto.Stakeholder;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberProfileFindResponse {

    @ApiModelProperty(value = "유저 id", example = "1")
    private Long memberId;
    @ApiModelProperty(value = "유저 이름", example = "홍길동")
    private String memberName;
    @ApiModelProperty(value = "유저 키", example = "178")
    private int memberHeight;
    @ApiModelProperty(value = "유저 몸무게", example = "66")
    private int memberWeight;
    @ApiModelProperty(value = "유저가 등록한 헬스장", example = "OO 헬스장")
    private String memberRegisteredGymName;
    @ApiModelProperty(value = "유저 프로필 사진 url (null 이면 asset의 기본 프로필 불러오기)", example = "profile_image_saved_url")
    private String customProfileImageUrl;
}
