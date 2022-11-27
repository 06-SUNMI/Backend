package capstone.everyhealth.exception.Sns;

public class SnsCommentNotFound extends Exception {
    public SnsCommentNotFound(Long id) {
        super("id가 " + id + "인 sns comment가 없음");
    }
}
