package capstone.everyhealth.exception.report;

public class ChallengeAuthPostReportNotFound extends Exception{
    public ChallengeAuthPostReportNotFound(Long id){
        super("id가 "+id+"인 챌린지 인증 신고글이 없음");
    }
}
