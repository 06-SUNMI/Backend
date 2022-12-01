package capstone.everyhealth.controller;

import capstone.everyhealth.domain.challenge.Challenge;
import capstone.everyhealth.domain.challenge.ChallengeParticipant;
import capstone.everyhealth.domain.challenge.ChallengeRoutine;
import capstone.everyhealth.domain.challenge.ChallengeRoutineContent;
import capstone.everyhealth.domain.enums.ChallengeStatus;
import capstone.everyhealth.domain.routine.MemberRoutine;
import capstone.everyhealth.domain.routine.MemberRoutineContent;
import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.exception.challenge.*;
import capstone.everyhealth.exception.memberroutine.MemberRoutineContentNotFound;
import capstone.everyhealth.exception.memberroutine.MemberRoutineNotFound;
import capstone.everyhealth.service.ChallengeService;
import capstone.everyhealth.service.MemberRoutineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

//@Component
//@EnableScheduling
@RequiredArgsConstructor
@Slf4j
@RestController
@Api(tags = {"특정 로직 강제 실행"})
public class DataManipulator {

    private final ChallengeService challengeService;
    private final MemberRoutineService routineService;

    //@PostConstruct
    //@Scheduled(cron = "1 0 0 * * 0")

    @ApiOperation(
            value = "챌린지 매주 검사 강제 실행",
            notes = "실제 챌린지 매주 수행 여부 검사를 현장에서 보여주기에 제약이 있으므로 (실제 수행 : 일 ㅡ> 월 자정)\n"
                    + "실행 즉시 해당 주의 챌린지 수행 여부 검사를 수행하도록 한다."
    )
    @GetMapping("/data-manipulator/activate-scheduler-immediately")
    public String checkChallengeParticipantProgress() throws ChallengeNotFound, ChallengeParticipantNotFound {

        //

        //    매주 토 ~ 일 넘어가는 자정에 검사
        //    매일 검사 안하는 이유 - 1주차 예시서 누구는 월화수 , 누구는 목금토에 할 수 있으므로

        //

        for (Challenge challenge : challengeService.findAllOpenChallenges()) {

            // 종료된 챌린지는 검사 X
            if (validateIsChallengeFinished(challenge)) {
                continue;
            }

            // 시작일 - 월요일, 종료일 - 일 ~ 월 넘어가는 자정으로 고정돼 있음
            int currentChallengeWeek = calculateCurrentChallengeWeek(challenge);
            LocalDate challengeEndDate = changeTypeStringToLocalDate(challenge.getEndDate());

            for (ChallengeParticipant challengeParticipant : challengeService.findChallengeParticipantListByChallenge(challenge)) {

                // 챌린지 실패자 마지막날 종료 처리
                if (challengeParticipant.getChallengeStatus() == ChallengeStatus.FAIL
                        && LocalDate.now().isEqual(challengeEndDate.plusDays(1))) {

                    challengeService.updateChallengeStatusFinished(challengeParticipant.getChallenge().getId());
                    break;
                }

                // 그 주에 인증 해야하는 루틴 수
                int targetCompletedRoutinesNum = currentChallengeWeek * challenge.getNumPerWeek();
                log.info("LocalDate.now() = {}",LocalDate.now());
                log.info("challengeEndDate.plusDays(0) = {}",challengeEndDate.plusDays(0));
                // ex) 총 2주, 주 3회 루틴의 경우 1주차에 3개, 2주차에 6개 완료해야 성공 그 미만은 실패 처리
                if (challengeParticipant.getCompletedRoutinesNum() < targetCompletedRoutinesNum) {
                    challengeService.updateChallengeParticipantStatus(challengeParticipant.getId(), ChallengeStatus.FAIL);
                }
                // 해당 주 루틴 다 인증 & 그 날이 챌린지 마지막 날 (테스트 용으로 지금은 1 ㅡ> 0으로 수정)

                else if (LocalDate.now().isEqual(challengeEndDate.plusDays(0))) {
                    log.info("OOOOOOOOOOOO");
                    challengeService.updateChallengeParticipantStatus(challengeParticipant.getId(), ChallengeStatus.SUCCESS);
                    challengeService.updateChallengeStatusFinished(challengeParticipant.getChallenge().getId());
                }
            }
        }
        return "강제 수행 완료";
    }

    private int calculateCurrentChallengeWeek(Challenge challenge) {

        LocalDate challengeStartDate = changeTypeStringToLocalDate(challenge.getStartDate());
        int challengeTotalWeek = challenge.getChallengeRoutineList().size() / challenge.getNumPerWeek();
        int currentChallengeWeek = 0;

        for (int currentWeek = 1; currentWeek <= challengeTotalWeek; currentWeek++) {

            LocalDate challengeCurrentWeekStartDate = challengeStartDate.plusWeeks(currentWeek - 1);
            LocalDate challengeCurrentWeekEndDate = challengeCurrentWeekStartDate.plusDays(6);

            log.info("LocalDate.now() = {}", LocalDate.now());
            log.info("challengeCurrentWeekStartDate.minusDays(1) = {}", challengeCurrentWeekStartDate.minusDays(1));
            log.info("challengeCurrentWeekEndDate.plusDays(1) = {}", challengeCurrentWeekEndDate.plusDays(1));

            if (LocalDate.now().isAfter(challengeCurrentWeekStartDate.minusDays(1))
                    && LocalDate.now().isBefore(challengeCurrentWeekEndDate.plusDays(1))) {
                currentChallengeWeek = currentWeek;
            }
        }

        return currentChallengeWeek;
    }

    @ApiOperation(
            value = "챌린지 루틴 강제 완료",
            notes = "챌린지 매주 수행 여부 검사는 챌린지 참여자의 루틴 완료 수로 판단하는데\n"
                    + "ex) 총 2주, 주 2회 루틴의 경우 1주차에 2개, 2주차에 4개의 루틴이 완료되어야 pass\n"
                    + "챌린지 루틴 인증이 당일 날만 가능해 챌린지 완료 루틴 수를 체크하는\n"
                    + "챌린지 수행 여부 테스트에 제약이 있으므로\n"
                    + "실행 즉시 해당 루틴 체크 완료 및 인증 과정을 강제로 수행시킨다.\n"
                    + "※ 수행 전 체크된게 없어야하고 인증쪽은 사진 없이 인증됨"
    )
    @GetMapping("/data-manipulator/complete-challenge-auth-immediately/challenge-routines/{challengeRoutineId}/member-routines/{memberRoutineId}")
    public String completeChallengeAuthImmediately(@ApiParam(value = "챌린지 루틴 id 값") @PathVariable Long challengeRoutineId,
                                                   @ApiParam(value = "멤버 루틴 id 값") @PathVariable Long memberRoutineId) throws MemberRoutineNotFound, DuplicateChallengeAuthInRoutine, ChallengeRoutineNotFound, ChallengeParticipantNotFound, NotAllRoutineContentsProgressedInChallenge, MemberRoutineContentNotFound {
        MemberRoutine memberRoutine = routineService.findRoutineByRoutineId(memberRoutineId);

        for (MemberRoutineContent memberRoutineContent : memberRoutine.getMemberRoutineContentList()) {
            routineService.updateRoutineContentCheck(memberRoutineContent.getId());
        }
        challengeService.challengeAuthPostForTest(challengeRoutineId, memberRoutineId);

        return "강제 인증 완료";
    }

    private boolean validateIsChallengeFinished(Challenge challenge) {
        return challenge.isFinished();
    }

    private LocalDate changeTypeStringToLocalDate(String localDate){
        return LocalDate.parse(localDate, DateTimeFormatter.ISO_DATE);
    }

    private String changeTypeLocalDateToString(LocalDate localDate){
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
