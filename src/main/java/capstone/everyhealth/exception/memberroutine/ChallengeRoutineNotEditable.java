package capstone.everyhealth.exception.memberroutine;

public class ChallengeRoutineNotEditable extends Exception{
    public ChallengeRoutineNotEditable(){
        super("챌린지 루틴은 수정이 불가능합니다.");
    }
}
