package capstone.everyhealth.domain.challenge;

import capstone.everyhealth.controller.dto.Challenge.ChallengePostOrUpdateRequest;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Challenge {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeRoutine> challengeRoutineList = new ArrayList<>();

    private String name;
    private String startDate;
    private String endDate;
    private int participationFee;
    private int participationNum;
    private String preparations;
    private int numPerWeek;

    public void updateContent(ChallengePostOrUpdateRequest challengePostOrUpdateRequest) {

        name = challengePostOrUpdateRequest.getChallengeName();
        startDate = challengePostOrUpdateRequest.getChallengeStartDate();
        endDate = challengePostOrUpdateRequest.getChallengeEndDate();
        participationFee = challengePostOrUpdateRequest.getChallengeParticipationFee();
        participationNum = challengePostOrUpdateRequest.getChallengeParticipationNum();
        preparations = challengePostOrUpdateRequest.getChallengePreparations();
        numPerWeek = challengePostOrUpdateRequest.getChallengeNumPerWeek();
    }
}
