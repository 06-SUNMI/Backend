package capstone.everyhealth.exception.report;

public class WriterEqualsReporter extends Exception{
    public WriterEqualsReporter(){
        super("글 작성자와 신고자가 동일합니다.");
    }
}
