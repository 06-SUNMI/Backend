package capstone.everyhealth.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class MemberRoutineRegisterDto {

    @ApiModelProperty(
            position = 0,
            value = "운동 이름 리스트",
            example = "[\"벤치 프레스 (덤벨)\", \"렛 풀다운\", \"플랭크\"]"
    )
    private List<String> typeList = new ArrayList<>();

    @ApiModelProperty(
            position = 1,
            value = "운동 부위 리스트",
            example = "[\"가슴\", \"등\", \"코어\"]"
    )
    private List<String> targetList = new ArrayList<>();

    @ApiModelProperty(
            position = 2,
            value = "운동 횟수 리스트",
            example = "[10, 20, null]"
    )
    private List<Integer> countList = new ArrayList();

    @ApiModelProperty(
            position = 3,
            value = "운동 중량 리스트",
            example = "[50, 55, null]"
    )
    private List<Integer> weightList = new ArrayList();

    @ApiModelProperty(
            position = 4,
            value = "운동 세트 리스트",
            example = "[2, 3, 4]"
    )
    private List<Integer> setList = new ArrayList();

    @ApiModelProperty(
            position = 5,
            value = "운동 시간 리스트 (초 단위)\n",
            example = "[null, null, 60]"
    )
    private List<Integer> timeList = new ArrayList<>();

    @ApiModelProperty(
            position = 6,
            value = "루틴 등록 날짜",
            example = "2022-11-02"
    )
    private String date;
}
