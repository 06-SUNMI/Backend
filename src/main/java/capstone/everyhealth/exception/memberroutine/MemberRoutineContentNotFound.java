package capstone.everyhealth.exception.memberroutine;

public class MemberRoutineContentNotFound extends Exception{
    public MemberRoutineContentNotFound(Long id){
        super("Id가 "+id+" 인 member routine content가 존재하지 않음");
    }
}
