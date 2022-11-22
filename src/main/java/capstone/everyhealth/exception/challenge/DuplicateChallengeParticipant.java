package capstone.everyhealth.exception.challenge;

public class DuplicateChallengeParticipant extends Exception{
    public DuplicateChallengeParticipant(){
        super("이미 챌린지에 참여 중 입니다.");
    }
}
