package capstone.everyhealth.controller.dto.Challenge;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengePostOrUpdateRequest {

    @ApiModelProperty(value = "챌린지 이름", example = "OO 챌린지")
    private String challengeName;

    /*@ApiModelProperty(
            value = "챌린지 루틴 목록"

    )
    private List<ChallengeRoutineData> challengeRoutineDataList = new ArrayList<>();*/

    @ApiModelProperty(value = "챌린지 시작일", example = "2022-12-01")
    private String challengeStartDate;

    @ApiModelProperty(value = "챌린지 종료일", example = "2022-12-31")
    private String challengeEndDate;

    @ApiModelProperty(value = "챌린지 참가비", example = "30000")
    private int challengeParticipationFee;

    @ApiModelProperty(value = "챌린지 현재까지 참가 인원 수? 최대 인원 수? (미정)", example = "123")
    private int challengeParticipationNum;

    @ApiModelProperty(value = "챌린지 유의 사항", example = "유의 사항 내용")
    private String challengePreparations;

    @ApiModelProperty(value = "주 당 챌린지 참여 횟수", example = "3")
    private int challengeNumPerWeek;
}
