package capstone.everyhealth;

import capstone.everyhealth.domain.routine.Workout;
import capstone.everyhealth.domain.routine.WorkoutData;
import capstone.everyhealth.domain.stakeholder.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit() {

            saveMember();
            saveWorkout();
        }

        private void saveWorkout() {

            for (String target : WorkoutData.workoutMap.keySet()) {
                for (String type : WorkoutData.workoutMap.get(target)) {

                    Workout workout = Workout.builder()
                            .target(target)
                            .type(type)
                            .build();

                    em.persist(workout);
                }
            }
        }

        public void saveMember() {

            for (int i = 0; i < 4; i++) {
                Member member = new Member();
                em.persist(member);
            }
        }
    }
}
