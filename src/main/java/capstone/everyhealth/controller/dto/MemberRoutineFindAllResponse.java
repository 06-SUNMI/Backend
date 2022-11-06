package capstone.everyhealth.controller.dto;

import capstone.everyhealth.domain.routine.MemberRoutine;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class MemberRoutineFindAllResponse {

    @ApiModelProperty(
            value = "사용자가 등록한 루틴 리스트",
            example = "["
                    + "{"
                    + "\"routineId\": 3,"
                    + "\"routineRegisterData\": \"2022-11-02\""
                    + "},"
                    + "{"
                    + "\"routineId\": 4,"
                    + "\"routineRegisterData\": \"2022-11-30\""
                    + "}"
                    + "]"
    )
    private List<MemberRoutineData> memberRoutineDataList = new ArrayList<>();

    public MemberRoutineFindAllResponse(List<MemberRoutine> memberRoutineList) {

        for (MemberRoutine memberRoutine : memberRoutineList) {

            Long routineId = memberRoutine.getId();
            String routineRegisterDate = memberRoutine.getRoutineRegisterdate();
            MemberRoutineData memberRoutineData = new MemberRoutineData(routineId, routineRegisterDate);

            memberRoutineDataList.add(memberRoutineData);
        }
    }

    @Data
    @AllArgsConstructor
    class MemberRoutineData {

        private Long routineId;
        private String routineRegisterData;
    }
}
