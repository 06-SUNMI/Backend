package capstone.everyhealth.exception.challenge;

public class MismatchChallengeRoutineProgressDateNumPerWeek extends Exception {
    public MismatchChallengeRoutineProgressDateNumPerWeek(){
        super("각 주에 정해진 수의 루틴만 선택해야 합니다.");
    }
}
