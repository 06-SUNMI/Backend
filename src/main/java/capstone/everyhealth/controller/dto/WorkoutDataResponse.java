package capstone.everyhealth.controller.dto;

import capstone.everyhealth.domain.routine.WorkoutName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutDataResponse {

    private List<WorkoutName> chestWorkoutList = new ArrayList<>();
    private List<WorkoutName> backWorkoutList = new ArrayList<>();
    private List<WorkoutName> lowerBodyWorkoutList = new ArrayList<>();
    private List<WorkoutName> shoulderWorkoutList = new ArrayList<>();
    private List<WorkoutName> tricepsWorkoutList = new ArrayList<>();
    private List<WorkoutName> bicepsWorkoutList = new ArrayList<>();
    private List<WorkoutName> coreWorkoutList = new ArrayList<>();
    private List<WorkoutName> forearmWorkoutList = new ArrayList<>();
    private List<WorkoutName> aerobicWorkoutList = new ArrayList<>();
}
