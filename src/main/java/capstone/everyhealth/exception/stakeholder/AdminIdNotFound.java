package capstone.everyhealth.exception.stakeholder;

public class AdminIdNotFound extends Exception{
    public AdminIdNotFound(String id){
        super("id가 "+id+"인 admin이 없습니다.");
    }
}
