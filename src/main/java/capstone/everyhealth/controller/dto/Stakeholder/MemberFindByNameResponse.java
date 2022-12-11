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
public class MemberFindByNameResponse {

    @ApiModelProperty(value="멤버 id",example = "1")
    private Long memberId;
    @ApiModelProperty(value="멤버 이름",example = "홍길동")
    private String name;
    @ApiModelProperty(value="멤버 이메일",example = "asdf@naver.com")
    private String email;
    @ApiModelProperty(value="멤버 키",example = "123")
    private int height;
    @ApiModelProperty(value="멤버 몸무게",example = "123")
    private int weight;
    @ApiModelProperty(value="소셜 로그인한 곳",example = "KAKAO")
    private String loginAt;
    @ApiModelProperty(value="소셜 로그인한 곳 고유 id",example = "12313131")
    private String socialId;
    @ApiModelProperty(value="등록 헬스장",example = "OO 헬스장")
    private String gymName;
    @ApiModelProperty(value="헬스장 고유 id",example = "123123")
    private String gymId;
    @ApiModelProperty(value="챌린지 참가로 획득한 포인트",example = "10000")
    private int point;
    @ApiModelProperty(value="유저 프로필 사진 url")
    private String customProfileImageUrl;
}
