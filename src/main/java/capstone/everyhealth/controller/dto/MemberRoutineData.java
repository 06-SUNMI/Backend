package capstone.everyhealth.controller.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRoutineData {

    @ApiModelProperty(value = "루틴의 id값", example = "1")
    private Long routineId;
    @ApiModelProperty(value = "루틴 등록 날짜", example = "2022-11-23")
    private String routineRegisterDate;
}
