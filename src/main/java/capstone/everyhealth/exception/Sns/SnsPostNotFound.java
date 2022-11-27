package capstone.everyhealth.exception.Sns;

public class SnsPostNotFound extends Exception {
    public SnsPostNotFound(Long snsPostId) {
        super("id가 " + snsPostId + "인 sns 작성 글이 없음");
    }
}
