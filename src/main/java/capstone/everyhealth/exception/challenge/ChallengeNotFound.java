package capstone.everyhealth.exception.challenge;

public class ChallengeNotFound extends Exception {
    public ChallengeNotFound(Long id) {
        super("Id가 " + id + " 인 challenge가 존재 하지 않음");
    }
}
