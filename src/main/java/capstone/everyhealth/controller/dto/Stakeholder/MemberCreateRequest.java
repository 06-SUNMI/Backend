package capstone.everyhealth.controller.dto.Stakeholder;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberCreateRequest{

    @ApiModelProperty(value = "유저 이름",example = "홍길동")
    private String memberName;
    @ApiModelProperty(value = "유저 이메일",example = "asdf1234@naver.com")
    private String memberEmail;
    @ApiModelProperty(value = "소셜 로그인한 곳",example = "KAKAO")
    private String loginAt;
    @ApiModelProperty(value ="소셜 로그인 한 곳에서 등록돼 있는 계정의 유저 고유 식별자 (id) 값",example = "123321")
    private String socialAccountId;
    @ApiModelProperty(value = "유저 키",example = "178")
    private int memberHeight;
    @ApiModelProperty(value = "유저 몸무게",example = "66")
    private int memberWeight;
    @ApiModelProperty(value = "유저가 등록한 헬스장 이름",example = "OO 헬스장")
    private String memberRegisteredGymName;
    @ApiModelProperty(value = "동명의 헬스장을 구별하기 위한 유저가 등록한 헬스장의 고유 id 값(카카오 로컬 API서 제공)", example = "13213132")
    private String memberRegisteredGymId;
    @ApiModelProperty(value="휴대 전화 번호",example = "010-1234-5678")
    private String memberPhoneNumber;
    @ApiModelProperty(value="프로필 사진 이미지 url")
    private String memberProfileUrl;
}
