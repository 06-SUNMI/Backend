package capstone.everyhealth.controller;

import capstone.everyhealth.controller.dto.Challenge.*;
import capstone.everyhealth.domain.challenge.Challenge;
import capstone.everyhealth.domain.challenge.ChallengeRoutine;
import capstone.everyhealth.domain.challenge.ChallengeRoutineContent;
import capstone.everyhealth.domain.routine.Workout;
import capstone.everyhealth.domain.routine.WorkoutName;
import capstone.everyhealth.service.ChallengeService;
import capstone.everyhealth.service.WorkoutService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Api(tags = {"챌린지 API"})
@Slf4j
public class ChallengeController {

    private final ChallengeService challengeService;
    private final WorkoutService workoutService;

    @ApiOperation(
            value = "챌린지 등록하기 by Admin",
            notes = "챌린지를 등록한다.\n"
                    + "챌린지 상세 내용을 보내면 챌린지 등록이 완료된다.\n"
                    + "반환 값은 챌린지의 id로, 이를 통해 챌린지 조회, 수정, 삭제를 할 수 있다.\n\n"
                    + "※ challengeNumPerWeek : 챌린지 주당 루틴 수 ex) 총 4주, 주당 3회면 3\n\n"
                    + "challengeRoutineDataList : 챌린지 루틴 리스트 \n"
                    + "challengeRoutineDataList는\n"
                    + "challengeProgressWeek(현재 챌린지 주차 ex) 3주차면 3)과\n"
                    + "challengeRoutineContentDataList(루틴 상세 내용)으로 구성\n\n"
                    + "challengeRoutineContentDataList 리스트엔 ChallengeRoutineContentData들이 들어 있음\n"
                    + "ChallengeRoutineContentData 클래스 - 내부에 challengeRoutineWorkoutName부터 challengeRoutineWorkoutTime까지 5개 필드를 가짐\n"
                    + "※ 필드 명 반드시 저거랑 일치해야함, 이 부분 잘 안되면 바로 연락 주세요\n"
    )
    @Transactional
    @PostMapping("/challenges")
    public Long save(@ApiParam(value = "등록할 챌린지 상세 내용") @RequestBody ChallengePostOrUpdateRequest challengePostOrUpdateRequest) {

        Challenge challenge = createChallenge(challengePostOrUpdateRequest);
        setupRelation(challenge);

        return challengeService.save(challenge);
    }

    @ApiOperation(
            value = "챌린지 전체 조회 by Member, Admin",
            notes = "등록된 전체 챌린지를 조회한다.\n"
                    + "관리자는 관리자 페이지서, 사용자는 앱 내 챌린지 페이지서 전체 챌린지를 조회할 때 사용한다.\n"
                    + "여기선 챌린지 목록만 보여주는 거라 겉 내용만. 루틴 및 세부 내용은 X\n"
                    + "상세 내용은 반환 값 중 id를 이용하여 상세 조회\n"
    )

    @GetMapping("/challenges")
    public ChallengeFindAllResponse findAll() {

        List<ChallengeFindResponse> challengeFindResponseList = new ArrayList<>();
        List<Challenge> challengeList = challengeService.findAll();

        for (Challenge challenge : challengeList) {

            ChallengeFindResponse challengeFindResponse = createChallengeFindResponse(challenge);
            challengeFindResponseList.add(challengeFindResponse);
        }

        return ChallengeFindAllResponse.builder().challengeFindResponseList(challengeFindResponseList).build();
    }

    @ApiOperation(
            value = "챌린지 상세 조회 by Member, Admin",
            notes = "선택한 챌린지의 상세 내용을 조회한다.\n"
                    + "관리자는 관리자 페이지서, 사용자는 앱 내 챌린지 페이지서 챌린지 상세 내용을 조회할 때 사용한다.\n"
    )
    @GetMapping("/challenges/{challengeId}")
    public ChallengeRoutinesFindResponse find(@ApiParam(value = "챌린지 id값", example = "1") @PathVariable Long challengeId) {

        Challenge challenge = challengeService.find(challengeId);
        List<ChallengeRoutineData> challengeRoutineDataList = new ArrayList<>();

        for (ChallengeRoutine challengeRoutine : challenge.getChallengeRoutineList()) {

            ChallengeRoutineData challengeRoutineData = createChallengeRoutineData(challengeRoutine);

            for (ChallengeRoutineContent challengeRoutineContent : challengeRoutine.getChallengeRoutineContentList()) {

                ChallengeRoutineContentData challengeRoutineContentData = createChallengeRoutineContentData(challengeRoutineContent);
                challengeRoutineData.getChallengeRoutineContentDataList().add(challengeRoutineContentData);
            }

            challengeRoutineDataList.add(challengeRoutineData);
        }

        ChallengeRoutinesFindResponse challengeRoutinesFindResponse = createChallengeRoutinesFindResponse(challengeRoutineDataList);

        return challengeRoutinesFindResponse;
    }

    @ApiOperation(
            value = "챌린지 수정 by Admin",
            notes = "등록한 챌린지의 내용을 수정한다."
    )
    @Transactional
    @PutMapping("/challenges/{challengeId}")
    public Long update(@ApiParam(value = "챌린지 수정 내용") @RequestBody ChallengePostOrUpdateRequest challengePostOrUpdateRequest,
                       @ApiParam(value = "챌린지 id값", example = "1") @PathVariable Long challengeId) {
        Challenge challenge = createChallenge(challengePostOrUpdateRequest);
        setupRelation(challenge);

        return challengeService.update(challenge, challengeId);
    }

    @ApiOperation(
            value = "챌린지 삭제 by Admin",
            notes = "등록한 챌린지를 삭제한다. 반환 값 X"
    )
    @Transactional
    @DeleteMapping("/challenges/{challengeId}")
    public void delete(@ApiParam(value = "챌린지 id값", example = "1") @PathVariable Long challengeId) {
        challengeService.delete(challengeId);
    }

    @ApiOperation(
            value = "(미완)챌린지 참가 by Member",
            notes = "챌린지에 참가하여 해당 챌린지의 루틴을 개인 루틴에 저장한다.\n"
                    + "참가 성공 시 챌린지 id를 반환한다."
    )
    @Transactional
    @PostMapping("/members/{memberId}/challenges/{challengeId}")
    public Long participate(@ApiParam(value = "멤버 id값", example = "1") @PathVariable Long memberId,
                            @ApiParam(value = "챌린지 id값", example = "1") @PathVariable Long challengeId) {

        challengeService.participate(memberId, challengeId);

        return challengeId;
    }

    private void setupRelation(Challenge challenge) {

        for (ChallengeRoutine challengeRoutine : challenge.getChallengeRoutineList()) {

            for (ChallengeRoutineContent challengeRoutineContent : challengeRoutine.getChallengeRoutineContentList()) {
                challengeRoutineContent.setChallengeRoutine(challengeRoutine);
            }
            challengeRoutine.setChallenge(challenge);
        }
    }

    private ChallengeRoutinesFindResponse createChallengeRoutinesFindResponse(List<ChallengeRoutineData> challengeRoutineDataList) {
        return ChallengeRoutinesFindResponse.builder()
                .challengeRoutineDataList(challengeRoutineDataList)
                .build();
    }

    private ChallengeRoutineContentData createChallengeRoutineContentData(ChallengeRoutineContent challengeRoutineContent) {
        return ChallengeRoutineContentData.builder()
                .challengeRoutineWorkoutSet(challengeRoutineContent.getChallengeRoutineWorkoutSet())
                .challengeRoutineWorkoutCount(challengeRoutineContent.getChallengeRoutineWorkoutCount())
                .challengeRoutineWorkoutName(challengeRoutineContent.getWorkout().getWorkoutName())
                .challengeRoutineWorkoutTime(challengeRoutineContent.getChallengeRoutineWorkoutTime())
                .challengeRoutineWorkoutWeight(challengeRoutineContent.getChallengeRoutineWorkoutWeight())
                .build();
    }

    private ChallengeRoutineData createChallengeRoutineData(ChallengeRoutine challengeRoutine) {
        return ChallengeRoutineData.builder()
                .ChallengeProgressWeek(challengeRoutine.getProgressWeek())
                .challengeRoutineContentDataList(new ArrayList<>())
                .build();
    }

    private ChallengeFindResponse createChallengeFindResponse(Challenge challenge) {
        return ChallengeFindResponse.builder()
                .challengeId(challenge.getId())
                .endDate(challenge.getEndDate())
                .name(challenge.getName())
                .numPerWeek(challenge.getNumPerWeek())
                .participationFee(challenge.getParticipationFee())
                .participationNum(challenge.getParticipationNum())
                .preparations(challenge.getPreparations())
                .startDate(challenge.getStartDate())
                .build();
    }

    private Challenge createChallenge(ChallengePostOrUpdateRequest challengePostRequest) {

        List<ChallengeRoutine> challengeRoutineList = new ArrayList<>();

        for (ChallengeRoutineData challengeRoutineData : challengePostRequest.getChallengeRoutineDataList()) {

            List<ChallengeRoutineContent> challengeRoutineContentList = new ArrayList<>();

            for (ChallengeRoutineContentData challengeRoutineContentData : challengeRoutineData.getChallengeRoutineContentDataList()) {

                Workout workout = createWorkout(challengeRoutineContentData);
                ChallengeRoutineContent challengeRoutineContent = createChallengeRoutineContent(challengeRoutineContentData, workout);

                challengeRoutineContentList.add(challengeRoutineContent);
            }

            ChallengeRoutine challengeRoutine = createChallengeRoutine(challengeRoutineData, challengeRoutineContentList);
            challengeRoutineList.add(challengeRoutine);
        }

        return Challenge.builder()
                .endDate(challengePostRequest.getChallengeEndDate())
                .startDate(challengePostRequest.getChallengeStartDate())
                .name(challengePostRequest.getChallengeName())
                .numPerWeek(challengePostRequest.getChallengeNumPerWeek())
                .participationFee(challengePostRequest.getChallengeParticipationFee())
                .preparations(challengePostRequest.getChallengePreparations())
                .challengeRoutineList(challengeRoutineList)
                .build();
    }

    private ChallengeRoutine createChallengeRoutine(ChallengeRoutineData challengeRoutineData, List<ChallengeRoutineContent> challengeRoutineContentList) {
        return ChallengeRoutine.builder()
                .challengeRoutineContentList(challengeRoutineContentList)
                .progressWeek(challengeRoutineData.getChallengeProgressWeek())
                .build();
    }

    private ChallengeRoutineContent createChallengeRoutineContent(ChallengeRoutineContentData challengeRoutineContentData, Workout workout) {
        return ChallengeRoutineContent.builder()
                .challengeRoutineWorkoutCount(challengeRoutineContentData.getChallengeRoutineWorkoutCount())
                .challengeRoutineWorkoutSet(challengeRoutineContentData.getChallengeRoutineWorkoutSet())
                .challengeRoutineWorkoutTime(challengeRoutineContentData.getChallengeRoutineWorkoutTime())
                .challengeRoutineWorkoutWeight(challengeRoutineContentData.getChallengeRoutineWorkoutWeight())
                .workout(workout)
                .build();
    }

    private Workout createWorkout(ChallengeRoutineContentData challengeRoutineContentData) {
        WorkoutName workoutName = challengeRoutineContentData.getChallengeRoutineWorkoutName();

        return workoutService.findByWorkoutName(workoutName);
    }
}
