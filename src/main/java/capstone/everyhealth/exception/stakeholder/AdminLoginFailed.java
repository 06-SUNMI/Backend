package capstone.everyhealth.exception.stakeholder;

public class AdminLoginFailed extends Exception{
    public AdminLoginFailed(){
        super("일치하는 관리자 계정이 없습니다.");
    }
}
