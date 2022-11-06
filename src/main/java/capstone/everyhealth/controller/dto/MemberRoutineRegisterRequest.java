package capstone.everyhealth.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class MemberRoutineRegisterRequest {

    @ApiModelProperty(
            value = "루틴 내 등록한 운동 목록",
            example = "[ "
                    + "{"
                    + "\"memberRoutineWorkoutName\" : \"PUSH_UP\","
                    + "\"memberRoutineWorkoutWeight\" : null,"
                    + "\"memberRoutineWorkoutCount\" : 20,"
                    + "\"memberRoutineWorkoutSet\" : 10,"
                    + "\"memberRoutineWorkoutTime\" : null"
                    + "},"
                    + "{"
                    + "\"memberRoutineWorkoutName\" : \"CHEST_PRESS_MACHINE\","
                    + "\"memberRoutineWorkoutWeight\" : 100,"
                    + "\"memberRoutineWorkoutCount\" : 10,"
                    + "\"memberRoutineWorkoutSet\" : 2,"
                    + "\"memberRoutineWorkoutTime\" : null"
                    + "}"
                    + "]"
    )
    private List<MemberRoutineWorkoutContent> memberRoutineWorkoutContentList = new ArrayList<>();

    @ApiModelProperty(
            value = "루틴 등록 날짜",
            example = "2022-11-02"
    )
    private String routineRegisterdate;
}
