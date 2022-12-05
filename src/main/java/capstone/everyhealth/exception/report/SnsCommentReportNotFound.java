package capstone.everyhealth.exception.report;

public class SnsCommentReportNotFound extends Exception {
    public SnsCommentReportNotFound(Long id) {
        super("id가 " + id + "인 sns 댓글 신고글이 없음");
    }
}
