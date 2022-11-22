package capstone.everyhealth.exception.memberroutine;

public class MemberRoutineNotFound extends Exception {
    public MemberRoutineNotFound(Long id) {
        super("error - 존재 하지 않는 routine" + " id : " + id);
    }
}
