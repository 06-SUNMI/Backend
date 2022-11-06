package capstone.everyhealth.domain.routine;

import lombok.Getter;

@Getter
public enum WorkoutName {

    BENCH_PRESS_DUMBBELL(WorkoutTarget.CHEST), CHEST_PRESS_MACHINE(WorkoutTarget.CHEST), PUSH_UP(WorkoutTarget.CHEST),

    LAT_PULL_DOWN(WorkoutTarget.BACK), ONE_ARM_DUMBBELL_ROW(WorkoutTarget.BACK), SEATED_ROW_CABLE(WorkoutTarget.BACK),

    SQUAT(WorkoutTarget.LOWER_BODY), WIDE_SQUAT(WorkoutTarget.LOWER_BODY), LEG_EXTENSION(WorkoutTarget.LOWER_BODY),

    SHOULDER_PRESS_DUMBBELL(WorkoutTarget.SHOULDER), LATERAL_RAISE_DUMBBELL(WorkoutTarget.SHOULDER), SHOULDER_PRESS_MACHINE(WorkoutTarget.SHOULDER),

    BENCH_DIPS(WorkoutTarget.TRICEPS), TRICEP_PUSH_DOWN_CABLE(WorkoutTarget.TRICEPS), ONE_ARM_TRICEP_EXTENSION_DUMBBELL(WorkoutTarget.TRICEPS),

    BICEP_CURL_DUMBBELL(WorkoutTarget.BICEPS), HAMMER_CURL_DUMBBELL(WorkoutTarget.BICEPS), BICEP_CURL_CABLE(WorkoutTarget.BICEPS),

    CRUNCH(WorkoutTarget.CORE), PLANK(WorkoutTarget.CORE), LEG_RAISE(WorkoutTarget.CORE),

    WRIST_CURL_DUMBBELL(WorkoutTarget.FOREARM), REVERSE_WRIST_CURL_DUMBBELL(WorkoutTarget.FOREARM), WRIST_CURL_BARBELL(WorkoutTarget.FOREARM),

    TREADMILL_RUNNING(WorkoutTarget.AEROBIC), SLOW_BURPEE(WorkoutTarget.AEROBIC), CRISS_CROSS_JUMP(WorkoutTarget.AEROBIC);

    private WorkoutTarget workoutTarget;

    WorkoutName(WorkoutTarget workoutTarget){
        this.workoutTarget = workoutTarget;
    }
}
