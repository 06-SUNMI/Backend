package capstone.everyhealth.exception.challenge;

public class SelectedDatesNumNotEqualsWithChallenge extends Exception{
    public SelectedDatesNumNotEqualsWithChallenge(){
        super("챌린지에 등록된 루틴 수와 일치하지 않습니다.");
    }
}
