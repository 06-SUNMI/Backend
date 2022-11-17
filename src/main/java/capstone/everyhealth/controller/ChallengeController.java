package capstone.everyhealth.controller;

import capstone.everyhealth.controller.dto.Challenge.*;
import capstone.everyhealth.controller.dto.Challenge.auth.ChallengeFindAllAuthPostData;
import capstone.everyhealth.controller.dto.Challenge.auth.ChallengeFindAllAuthPostResponse;
import capstone.everyhealth.domain.challenge.Challenge;
import capstone.everyhealth.domain.challenge.ChallengeAuthPost;
import capstone.everyhealth.domain.challenge.ChallengeRoutine;
import capstone.everyhealth.domain.challenge.ChallengeRoutineContent;
import capstone.everyhealth.domain.routine.Workout;
import capstone.everyhealth.domain.routine.WorkoutName;
import capstone.everyhealth.service.ChallengeService;
import capstone.everyhealth.service.WorkoutService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
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
    public List<ChallengeFindResponse> findAll() {

        List<ChallengeFindResponse> challengeFindResponseList = new ArrayList<>();
        List<Challenge> challengeList = challengeService.findAll();

        for (Challenge challenge : challengeList) {

            ChallengeFindResponse challengeFindResponse = createChallengeFindResponse(challenge);
            challengeFindResponseList.add(challengeFindResponse);
        }

        return challengeFindResponseList;
    }

    @ApiOperation(
            value = "특정 챌린지 상세 내용 조회 by Member, Admin",
            notes = "선택한 챌린지의 상세 내용을 조회한다.\n"
                    + "관리자는 관리자 페이지서, 사용자는 앱 내 챌린지 페이지서 특정 챌린지를 누를 때 사용한다.\n"
    )
    @GetMapping("/challenges/{challengeId}")
    public ChallengeFindDetailResponse find(@ApiParam(value = "챌린지 id값", example = "1") @PathVariable Long challengeId) {

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

        ChallengeFindDetailResponse challengeFindDetailResponse = createChallengeFindDetailResponse(challenge, challengeRoutineDataList);

        return challengeFindDetailResponse;
    }

    /*@ApiOperation(
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
    }*/


    @ApiOperation(
            value = "챌린지 삭제 by Admin",
            notes = "등록한 챌린지를 삭제한다. 반환 값 X"
    )
    @DeleteMapping("/challenges/{challengeId}")
    public void delete(@ApiParam(value = "챌린지 id값", example = "1") @PathVariable Long challengeId) {
        challengeService.delete(challengeId);
    }

    @ApiOperation(
            value = "챌린지 참가 by Member",
            notes = "챌린지에 참가하여 해당 챌린지의 루틴을 개인 루틴에 저장한다.\n"
                    + "참가 성공 시 챌린지 id를 반환한다."
    )
    @PostMapping("/members/{memberId}/challenges/{challengeId}")
    public Long participate(@ApiParam(value = "멤버 id값", example = "1") @PathVariable Long memberId,
                            @ApiParam(value = "챌린지 id값", example = "1") @PathVariable Long challengeId,
                            @ApiParam(value = "유저가 등록한 챌린지 루틴 별 수행 날짜") @RequestBody ChallengeRoutineCopyToParticipantRequest challengeRoutineCopyToParticipantRequest) {

        challengeService.participate(memberId, challengeId, challengeRoutineCopyToParticipantRequest);

        return challengeId;
    }

    @ApiOperation(
            value = "자신이 참가한 챌린지 목록 확인 by Member",
            notes = "멤버 자신이 참여한 챌린지 목록들을 확인한다."
    )
    @GetMapping("/challenges/members/{memberId}")
    public List<ChallengeFindByMemberResponse> findChallengesByMemberId(@ApiParam(value = "멤버의 id 값", example = "1") @PathVariable Long memberId) {

        List<ChallengeFindByMemberResponse> challengeFindByMemberResponseList = new ArrayList<>();
        Map<Challenge, Integer> challengeAndCompletedRoutinesNumMap = challengeService.findChallengeAndCompletedRoutinesNumMapByMemberId(memberId);

        for (Map.Entry<Challenge, Integer> entry : challengeAndCompletedRoutinesNumMap.entrySet()) {

            ChallengeFindByMemberResponse challengeFindByMemberResponse = createChallengeFindByMemberResponse(entry.getKey());
            challengeFindByMemberResponse.setProgressRate(calculateProgressRate(entry.getKey(), entry.getValue()));
            challengeFindByMemberResponseList.add(challengeFindByMemberResponse);
        }

        return challengeFindByMemberResponseList;
    }

    @ApiOperation(
            value = "챌린지 사진 인증 by Member",
            notes = "멤버 자신이 참여한 챌린지에서의 인증 사진을 올린다.\n"
                    + "멤버 루틴 조회 ㅡ> 챌린지 인증 버튼 누르기 (챌린지 루틴만 있음) ㅡ> "
    )
    @PostMapping("/challenges/auth/challenge-routines/{challengeRoutineId}/members/{memberId}")
    public Long challengeAuthPost(@ApiParam(value = "챌린지 루틴의 id 값", example = "1") @PathVariable Long challengeRoutineId,
                                  @ApiParam(value = "멤버의 id 값", example = "1") @PathVariable Long memberId,
                                  @ApiParam(value = "챌린지 인증 사진 파일") @RequestPart MultipartFile challengeAuthPostPhoto
            /*@ApiParam(value = "루틴 날짜",example="2022-11-15") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate currentDate*/) {
        return challengeService.challengeAuthPost(challengeRoutineId, memberId, challengeAuthPostPhoto);
    }

    @ApiOperation(
            value = "챌린지 전체 인증 글 보기 by Member",
            notes = "멤버 자신이 참여한 챌린지에서 올라온 모든 인증 글을 불러온다."
    )
    @GetMapping("/challenges/{challengeId}/auth")
    public ChallengeFindAllAuthPostResponse findAllChallengeAuthPost(@ApiParam(value = "챌린지 id 값", example = "1") @PathVariable Long challengeId) {

        List<ChallengeAuthPost> challengeAuthPostList = challengeService.findAllChallengeAuthPost(challengeId);
        List<ChallengeFindAllAuthPostData> challengeFindAllAuthPostDataList = new ArrayList<>();

        for (ChallengeAuthPost challengeAuthPost : challengeAuthPostList) {

            ChallengeFindAllAuthPostData challengeFindAllAuthPostData = createChallengeFindAllAuthPostData(challengeAuthPost);
            challengeFindAllAuthPostDataList.add(challengeFindAllAuthPostData);
        }

        return new ChallengeFindAllAuthPostResponse(challengeFindAllAuthPostDataList);
    }


    private ChallengeFindAllAuthPostData createChallengeFindAllAuthPostData(ChallengeAuthPost challengeAuthPost) {
        return ChallengeFindAllAuthPostData.builder()
                .memberId(challengeAuthPost.getMember().getId())
                .challengeAuthPostId(challengeAuthPost.getId())
                .photoUrl(challengeAuthPost.getPhotoUrl())
                .build();
    }

    private int calculateProgressRate(Challenge challenge, Integer completedChallengeRoutineNum) {
        return 100 * completedChallengeRoutineNum / challenge.getChallengeRoutineList().size();
    }

    private ChallengeFindByMemberResponse createChallengeFindByMemberResponse(Challenge challenge) {
        return ChallengeFindByMemberResponse.builder()
                .challengeId(challenge.getId())
                .endDate(challenge.getEndDate())
                .name(challenge.getName())
                .numPerWeek(challenge.getNumPerWeek())
                .participationFee(challenge.getParticipationFee())
                .participationNum(challenge.getParticipationNum())
                .startDate(challenge.getStartDate())
                .build();
    }

    private void setupRelation(Challenge challenge) {

        for (ChallengeRoutine challengeRoutine : challenge.getChallengeRoutineList()) {

            for (ChallengeRoutineContent challengeRoutineContent : challengeRoutine.getChallengeRoutineContentList()) {
                challengeRoutineContent.setChallengeRoutine(challengeRoutine);
            }
            challengeRoutine.setChallenge(challenge);
        }
    }

    private ChallengeFindDetailResponse createChallengeFindDetailResponse(Challenge challenge, List<ChallengeRoutineData> challengeRoutineDataList) {
        return ChallengeFindDetailResponse.builder()
                .challengeId(challenge.getId())
                .name(challenge.getName())
                .numPerWeek(challenge.getNumPerWeek())
                .participationNum(challenge.getParticipationNum())
                .participationFee(challenge.getParticipationFee())
                .startDate(challenge.getStartDate())
                .endDate(challenge.getEndDate())
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
                .challengeProgressWeek(challengeRoutine.getProgressWeek())
                .challengeRoutineId(challengeRoutine.getId())
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
