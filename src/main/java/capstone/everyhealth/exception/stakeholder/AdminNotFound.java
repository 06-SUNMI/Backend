package capstone.everyhealth.exception.stakeholder;

public class AdminNotFound extends Exception {
    public AdminNotFound(Long id) {
        super("id가 " + id + " 인 관리자가 존재 하지 않음");
    }
}
