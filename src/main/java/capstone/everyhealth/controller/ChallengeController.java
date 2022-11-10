package capstone.everyhealth.controller;

import capstone.everyhealth.controller.dto.Challenge.ChallengeFindResponse;
import capstone.everyhealth.controller.dto.Challenge.ChallengePostOrUpdateRequest;
import capstone.everyhealth.domain.challenge.Challenge;
import capstone.everyhealth.service.ChallengeService;
import io.swagger.annotations.Api;
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

    @ApiOperation(
            value = "챌린지 등록하기 by Admin",
            notes = "챌린지를 등록한다.\n"
                    + "챌린지 상세 내용을 보내면 챌린지 등록이 완료된다.\n"
                    + "반환 값은 챌린지의 id로, 이를 통해 챌린지 조회, 수정, 삭제를 할 수 있다.\n"
    )
    @Transactional
    @PostMapping("/challenges")
    public Long save(@ApiParam(value = "등록할 챌린지 상세 내용") @RequestBody ChallengePostOrUpdateRequest challengePostOrUpdateRequest) {

        Challenge challenge = createChallenge(challengePostOrUpdateRequest);

        return challengeService.save(challenge);
    }

    @ApiOperation(
            value = "챌린지 전체 조회 by Member, Admin",
            notes = "등록된 전체 챌린지를 조회한다.\n"
                    + "관리자는 관리자 페이지서, 사용자는 앱 내 챌린지 페이지서 전체 챌린지를 조회할 때 사용한다.\n"
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
            value = "챌린지 상세 조회 by Member, Admin",
            notes = "선택한 챌린지의 상세 내용을 조회한다.\n"
                    + "관리자는 관리자 페이지서, 사용자는 앱 내 챌린지 페이지서 챌린지 상세 내용을 조회할 때 사용한다.\n"
    )
    @GetMapping("/challenges/{challengeId}")
    public ChallengeFindResponse find(@ApiParam(value = "챌린지 id값", example = "1") @PathVariable Long challengeId) {

        Challenge challenge = challengeService.find(challengeId);

        return createChallengeFindResponse(challenge);
    }

    @ApiOperation(
            value = "챌린지 수정 by Admin",
            notes = "등록한 챌린지의 내용을 수정한다."
    )
    @Transactional
    @PutMapping("/challenges/{challengeId}")
    public Long update(@ApiParam(value = "챌린지 수정 내용") @RequestBody ChallengePostOrUpdateRequest challengePostOrUpdateRequest,
                       @ApiParam(value = "챌린지 id값", example = "1") @PathVariable Long challengeId) {
        return challengeService.update(challengePostOrUpdateRequest, challengeId);
    }

    @ApiOperation(
            value = "챌린지 삭제 by Admin",
            notes = "등록한 챌린지를 삭제한다."
    )
    @Transactional
    @DeleteMapping("/challenges/{challengeId}")
    public void delete(@ApiParam(value = "챌린지 id값", example = "1") @PathVariable Long challengeId) {
        challengeService.delete(challengeId);
    }

    @ApiOperation(
            value = "챌린지 참가 by Member",
            notes = "챌린지에 참가하여 해당 챌린지의 루틴을 개인 루틴에 저장한다."
    )
    @Transactional
    @PostMapping("/members/{memberId}/challenges/{challengeId}")
    public Long participate(@ApiParam(value = "멤버 id값", example = "1") @PathVariable Long memberId,
                            @ApiParam(value = "챌린지 id값", example = "1") @PathVariable Long challengeId) {

        return 1L;
    }

    private ChallengeFindResponse createChallengeFindResponse(Challenge challenge) {
        return ChallengeFindResponse.builder()
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
        return Challenge.builder()
                .participationNum(challengePostRequest.getChallengeParticipationNum())
                .endDate(challengePostRequest.getChallengeEndDate())
                .startDate(challengePostRequest.getChallengeStartDate())
                .name(challengePostRequest.getChallengeName())
                .numPerWeek(challengePostRequest.getChallengeNumPerWeek())
                .participationFee(challengePostRequest.getChallengeParticipationFee())
                .preparations(challengePostRequest.getChallengePreparations())
                .build();
    }
}
