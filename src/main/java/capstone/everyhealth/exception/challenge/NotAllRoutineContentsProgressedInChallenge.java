package capstone.everyhealth.exception.challenge;

public class NotAllRoutineContentsProgressedInChallenge extends Exception{
    public NotAllRoutineContentsProgressedInChallenge(){
        super("루틴 내 모든 운동이 체크되어야 인증 가능합니다.");
    }
}
