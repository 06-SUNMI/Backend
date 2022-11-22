package capstone.everyhealth.exception.challenge;

public class DuplicateChallengeAuthInRoutine extends Exception{
    public DuplicateChallengeAuthInRoutine(){
        super("동일 루틴에 대하여 중복 인증 할 수 없습니다.");
    }
}
