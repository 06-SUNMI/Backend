package capstone.everyhealth.repository;

import capstone.everyhealth.domain.routine.Workout;
import capstone.everyhealth.domain.routine.WorkoutName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout,Long> {
    Workout findByWorkoutName(WorkoutName workoutName);
}
