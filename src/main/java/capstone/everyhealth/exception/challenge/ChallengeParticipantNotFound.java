package capstone.everyhealth.exception.challenge;

public class ChallengeParticipantNotFound extends Exception{
    public ChallengeParticipantNotFound(){
        super("해당 챌린지 참가자가 존재 하지 않음");
    }
}
