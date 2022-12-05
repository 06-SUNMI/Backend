package capstone.everyhealth.exception.report;

public class SnsPostReportNotFound extends Exception {
    public SnsPostReportNotFound(Long id) {
        super("id가 " + id + "인 sns 작성글 신고글이 없음");
    }
}
