/*package capstone.everyhealth.controller;

import capstone.everyhealth.domain.challenge.Challenge;
import capstone.everyhealth.domain.challenge.ChallengeParticipant;
import capstone.everyhealth.domain.enums.ChallengeStatus;
import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.exception.challenge.ChallengeNotFound;
import capstone.everyhealth.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class Scheduler {

    private final ChallengeService challengeService;

    @PostConstruct
    @Scheduled(cron = "1 0 0 * * 0")
    public void checkChallengeParticipantProgress() throws ChallengeNotFound {

        //

        //    매주 토 ~ 일 넘어가는 자정에 검사
        //    매일 검사 안하는 이유 - 1주차 예시서 누구는 월화수 , 누구는 목금토에 할 수 있으므로

        //

        for (Challenge challenge : challengeService.findAll()) {

            // 종료된 챌린지는 검사 X
            if (validateIsChallengeFinished(challenge)) {
                continue;
            }

            // 시작일 - 월요일, 종료일 - 일 ~ 월 넘어가는 자정으로 고정돼 있음
            int currentChallengeWeek = calculateCurrentChallengeWeek(challenge);
            LocalDate challengeEndDate = LocalDate.parse(challenge.getStartDate(), DateTimeFormatter.ISO_DATE);

            for (ChallengeParticipant challengeParticipant : challengeService.findChallengeParticipantListByChallenge(challenge)) {

                int targetCompletedRoutinesNum = currentChallengeWeek * challenge.getNumPerWeek();
                // ex) 총 2주, 주 3회 루틴의 경우 1주차에 3개, 2주차에 6개 완료해야 성공 그 미만은 실패 처리
                if (challengeParticipant.getCompletedRoutinesNum() < targetCompletedRoutinesNum ) {
                    challengeParticipant.setChallengeStatus(ChallengeStatus.FAIL);
                } else if(LocalDate.now().isEqual(challengeEndDate.plusDays(1))){

                    challengeParticipant.setChallengeStatus(ChallengeStatus.SUCCESS);
                    challengeService.updateChallengeStatusFinished(challengeParticipant.getChallenge().getId());
                }
            }
        }
    }

    private int calculateCurrentChallengeWeek(Challenge challenge) {

        LocalDate challengeStartDate = LocalDate.parse(challenge.getStartDate(), DateTimeFormatter.ISO_DATE);
        int challengeTotalWeek = challenge.getChallengeRoutineList().size() / challenge.getNumPerWeek();
        int currentChallengeWeek = 0;

        for (int currentWeek = 1; currentWeek <= challengeTotalWeek; currentWeek++) {

            LocalDate challengeCurrentWeekStartDate = challengeStartDate.plusWeeks(currentWeek - 1);
            LocalDate challengeCurrentWeekEndDate = challengeStartDate.plusDays(6);

            if (LocalDate.now().isAfter(challengeCurrentWeekStartDate.minusDays(1))
                    && LocalDate.now().isBefore(challengeCurrentWeekEndDate.plusDays(1))) {
                currentChallengeWeek = currentWeek;
            }
        }

        return currentChallengeWeek;
    }

    private boolean validateIsChallengeFinished(Challenge challenge) {
        return challenge.isFinished();
    }
}
*/
