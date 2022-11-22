package capstone.everyhealth.exception.challenge;

public class ChallengeRoutineNotFound extends Exception {
    public ChallengeRoutineNotFound(Long id) {
        super("Id가 " + id + " 인 챌린지 루틴이 존재 하지 않음");
    }
}
