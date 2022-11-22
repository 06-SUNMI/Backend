package capstone.everyhealth.exception.stakeholder;

public class MemberNotFound extends Exception {
    public MemberNotFound(Long id) {
        super("id가 " + id + " 인 멤버가 존재 하지 않음");
    }
}
