package capstone.everyhealth.controller;

import capstone.everyhealth.controller.dto.WorkoutDataResponse;
import capstone.everyhealth.domain.routine.Workout;
import capstone.everyhealth.domain.routine.WorkoutName;
import capstone.everyhealth.domain.routine.WorkoutTarget;
import capstone.everyhealth.service.WorkoutService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Api(
        tags={"운동 정보 API"}

)
public class WorkoutController {

    private final WorkoutService workoutService;

    @ApiOperation(
            value="운동 정보 가져오기",
            notes="운동 정보를 부위 별 리스트로 가져온다."
    )
    @GetMapping("/workout-data")
    public WorkoutDataResponse getWorkoutData() {

        List<Workout> workoutList = workoutService.findAll();
        WorkoutDataResponse workoutDataResponse;

        List<WorkoutName> chestWorkoutList = new ArrayList<>();
        List<WorkoutName> backWorkoutList = new ArrayList<>();
        List<WorkoutName> lowerBodyWorkoutList = new ArrayList<>();
        List<WorkoutName> shoulderWorkoutList = new ArrayList<>();
        List<WorkoutName> tricepsWorkoutList = new ArrayList<>();
        List<WorkoutName> bicepsWorkoutList = new ArrayList<>();
        List<WorkoutName> coreWorkoutList = new ArrayList<>();
        List<WorkoutName> forearmWorkoutList = new ArrayList<>();
        List<WorkoutName> aerobicWorkoutList = new ArrayList<>();

        addWorkoutName(
                workoutList, chestWorkoutList, backWorkoutList,
                lowerBodyWorkoutList, shoulderWorkoutList, tricepsWorkoutList,
                bicepsWorkoutList, coreWorkoutList, forearmWorkoutList, aerobicWorkoutList
        );

        workoutDataResponse = createWorkoutDataResponse(chestWorkoutList, backWorkoutList, lowerBodyWorkoutList, shoulderWorkoutList, tricepsWorkoutList, bicepsWorkoutList, coreWorkoutList, forearmWorkoutList, aerobicWorkoutList);

        return workoutDataResponse;
    }

    private WorkoutDataResponse createWorkoutDataResponse(List<WorkoutName> chestWorkoutList, List<WorkoutName> backWorkoutList, List<WorkoutName> lowerBodyWorkoutList, List<WorkoutName> shoulderWorkoutList, List<WorkoutName> tricepsWorkoutList, List<WorkoutName> bicepsWorkoutList, List<WorkoutName> coreWorkoutList, List<WorkoutName> forearmWorkoutList, List<WorkoutName> aerobicWorkoutList) {
        return WorkoutDataResponse.builder()
                .chestWorkoutList(chestWorkoutList)
                .backWorkoutList(backWorkoutList)
                .lowerBodyWorkoutList(lowerBodyWorkoutList)
                .shoulderWorkoutList(shoulderWorkoutList)
                .tricepsWorkoutList(tricepsWorkoutList)
                .bicepsWorkoutList(bicepsWorkoutList)
                .coreWorkoutList(coreWorkoutList)
                .forearmWorkoutList(forearmWorkoutList)
                .aerobicWorkoutList(aerobicWorkoutList)
                .build();
    }

    private void addWorkoutName(List<Workout> workoutList, List<WorkoutName> chestWorkoutList, List<WorkoutName> backWorkoutList, List<WorkoutName> lowerBodyWorkoutList, List<WorkoutName> shoulderWorkoutList, List<WorkoutName> tricepsWorkoutList, List<WorkoutName> bicepsWorkoutList, List<WorkoutName> coreWorkoutList, List<WorkoutName> forearmWorkoutList, List<WorkoutName> aerobicWorkoutList) {

        for (Workout workout : workoutList) {

            WorkoutTarget workoutTarget = workout.getWorkoutName().getWorkoutTarget();

            switch (workoutTarget) {

                case CHEST:
                    chestWorkoutList.add(workout.getWorkoutName());
                    break;

                case BACK:
                    backWorkoutList.add(workout.getWorkoutName());
                    break;

                case LOWER_BODY:
                    lowerBodyWorkoutList.add(workout.getWorkoutName());
                    break;

                case SHOULDER:
                    shoulderWorkoutList.add(workout.getWorkoutName());
                    break;

                case TRICEPS:
                    tricepsWorkoutList.add(workout.getWorkoutName());
                    break;

                case BICEPS:
                    bicepsWorkoutList.add(workout.getWorkoutName());
                    break;

                case CORE:
                    coreWorkoutList.add(workout.getWorkoutName());
                    break;

                case FOREARM:
                    forearmWorkoutList.add(workout.getWorkoutName());
                    break;

                case AEROBIC:
                    aerobicWorkoutList.add(workout.getWorkoutName());
                    break;
            }
        }
    }
}
