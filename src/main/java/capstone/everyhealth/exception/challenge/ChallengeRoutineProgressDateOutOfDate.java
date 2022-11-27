package capstone.everyhealth.exception.challenge;

public class ChallengeRoutineProgressDateOutOfDate extends Exception{
    public ChallengeRoutineProgressDateOutOfDate(){
        super("챌린지 범위 내의 날짜를 선택해야합니다.");
    }
}
