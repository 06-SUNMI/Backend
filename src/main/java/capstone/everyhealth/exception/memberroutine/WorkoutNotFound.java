package capstone.everyhealth.exception.memberroutine;

public class WorkoutNotFound extends Exception{
    public WorkoutNotFound(){
        super("DB 초기화 중 Workout Data 누락 - 백엔드한테 즉시 알려주세요");
    }
}
