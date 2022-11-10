package capstone.everyhealth.controller.dto.Stakeholder;

import capstone.everyhealth.domain.enums.LoginType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberCreateRequest {

    @ApiModelProperty(value = "유저 이름",example = "홍길동")
    private String memberName;
    @ApiModelProperty(value="소셜 로그인 한 곳",example = "카카오")
    private String loginAt;
    @ApiModelProperty(value ="소셜 로그인 한 곳에서 등록돼 있는 계정의 유저 고유 식별자 (id) 값",example = "123321")
    private String socialAccountId;
    @ApiModelProperty(value = "유저 키",example = "178")
    private int memberHeight;
    @ApiModelProperty(value = "유저 몸무게",example = "66")
    private int memberWeight;
    @ApiModelProperty(value = "유저가 등록한 헬스장",example = "OO 헬스장")
    private String memberRegisteredGymName;
}
