package capstone.everyhealth.controller.dto.MemberRoutine;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRoutineContentCheckRequest {

    @ApiModelProperty(
            value = "사용자가 체크한 운동 리스트",
            example = "["
                    + "1"
                    + "]"
    )
    private List<Long> checkedList = new ArrayList<>();

    @ApiModelProperty(
            value = "사용자가 체크 해제한 운동 리스트",
            example = "["
                    + "2"
                    + "]"
    )
    private List<Long> uncheckedList = new ArrayList<>();
}
