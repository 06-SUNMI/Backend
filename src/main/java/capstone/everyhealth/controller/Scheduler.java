package capstone.everyhealth.controller;

import capstone.everyhealth.domain.challenge.Challenge;
import capstone.everyhealth.domain.challenge.ChallengeParticipant;
import capstone.everyhealth.domain.enums.ChallengeStatus;
import capstone.everyhealth.domain.stakeholder.Member;
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
    @Scheduled(cron = "0 0 0 * * 1")
    public void run() {

        for (Challenge challenge : challengeService.findAll()) {

            LocalDate challengeStartDate = LocalDate.parse(challenge.getStartDate(), DateTimeFormatter.ISO_DATE);
            LocalDate challengeEndDate = LocalDate.parse(challenge.getEndDate(), DateTimeFormatter.ISO_DATE);

            // 시작일 - 월요일, 종료일 - 일 ~ 월 넘어가는 자정으로 고정돼 있음
            int challengeTotalWeek = (Period.between(challengeStartDate, challengeEndDate).getDays() + 1) / 7;
            int challengeTotalRoutinesNum = challengeTotalWeek * challenge.getNumPerWeek();

            if (!(LocalDate.now().isEqual(challengeEndDate))) {
                break;
            }

            for (ChallengeParticipant challengeParticipant : challengeService.findChallengeParticipantListByChallenge(challenge)) {

                if (challengeParticipant.getCompletedRoutinesNum() == challengeTotalRoutinesNum) {
                    challengeParticipant.setChallengeStatus(ChallengeStatus.SUCCESS);
                }else{
                    challengeParticipant.setChallengeStatus(ChallengeStatus.FAIL);
                }
            }
        }
    }
}
