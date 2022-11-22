package capstone.everyhealth.exception.challenge;

public class NotInChallengeRoutineProgressDateRange extends Exception {
    public NotInChallengeRoutineProgressDateRange(){
        super("각 주에 정해진 수의 루틴만 선택해야 합니다.");
    }
}
