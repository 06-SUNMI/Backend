package capstone.everyhealth.exception.report;

public class DuplicateReporter extends Exception{
    public DuplicateReporter(){
        super("이미 신고한 게시글입니다.");
    }
}
