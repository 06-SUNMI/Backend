package capstone.everyhealth;

import capstone.everyhealth.domain.routine.Workout;
import capstone.everyhealth.domain.routine.WorkoutName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Component
@Transactional
@RequiredArgsConstructor
public class InitService {

    private final EntityManager em;

    public void dbInit() {
        saveWorkout();
    }

    private void saveWorkout() {

        for (WorkoutName workoutName : WorkoutName.values()) {

            Workout workout = Workout.builder()
                    .workoutName(workoutName)
                    .workoutTarget(workoutName.getWorkoutTarget())
                    .build();

            em.persist(workout);
        }
    }
}
