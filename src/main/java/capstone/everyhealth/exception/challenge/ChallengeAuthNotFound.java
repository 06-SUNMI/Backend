package capstone.everyhealth.exception.challenge;

public class ChallengeAuthNotFound extends Exception {
    public ChallengeAuthNotFound(Long id) {
        super("id가 " + id + "인 챌린지 인증 글이 없음");
    }
}
