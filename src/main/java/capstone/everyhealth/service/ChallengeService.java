package capstone.everyhealth.service;

import capstone.everyhealth.controller.dto.Challenge.ChallengeRoutineCopyToParticipantData;
import capstone.everyhealth.controller.dto.Challenge.ChallengeRoutineCopyToParticipantRequest;
import capstone.everyhealth.domain.challenge.*;
import capstone.everyhealth.domain.routine.MemberRoutine;
import capstone.everyhealth.domain.routine.MemberRoutineContent;
import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.fileupload.service.FileUploadService;
import capstone.everyhealth.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final MemberRepository memberRepository;
    private final ChallengeRoutineRepository challengeRoutineRepository;
    private final MemberRoutineRepository memberRoutineRepository;
    private final ChallengeParticipantRepository challengeParticipantRepository;
    private final FileUploadService fileUploadService;
    private final ChallengeAuthPostRepository challengeAuthPostRepository;

    @Transactional
    public Long save(Challenge challenge) {
        return challengeRepository.save(challenge).getId();
    }

    public List<Challenge> findAll() {
        return challengeRepository.findAll();
    }

    public Challenge find(Long challengeId) {

        return challengeRepository.findById(challengeId).get();
    }

    /*@Transactional
    public Long update(Challenge challenge, Long challengeId) {

        Challenge prevChallenge = challengeRepository.findById(challengeId).get();

        updateExceptChallengeRoutineList(prevChallenge, challenge);
        clearRelation(prevChallenge);

        // 1. 기존 ch 전체 연결 끊기 o
        // 2. 새 ch-rou for - 기존 ch와 연결 o
        // 3. 새 ch와 ch-rou 연결 끊기 o
        // 4. 새 ch-rou-con for - 새 ch-rou와 연결 o

        for (ChallengeRoutine challengeRoutine : challenge.getChallengeRoutineList()) {

            challengeRoutine.setChallenge(prevChallenge);
            prevChallenge.getChallengeRoutineList().add(challengeRoutine);
        }

        challenge.getChallengeRoutineList().clear();

        return challengeId;
    }*/

    @Transactional
    public void delete(Long challengeId) {
        challengeRepository.deleteById(challengeId);
    }

    @Transactional
    public void participate(Long memberId, Long challengeId, ChallengeRoutineCopyToParticipantRequest challengeRoutineCopyToParticipantRequest) {

        Member member = memberRepository.findById(memberId).get();
        Challenge challenge = challengeRepository.findById(challengeId).get();

        for (ChallengeRoutineCopyToParticipantData challengeRoutineCopyToParticipantData : challengeRoutineCopyToParticipantRequest.getChallengeRoutineCopyToParticipantDataList()) {

            ChallengeRoutine challengeRoutine = challengeRoutineRepository.findById(challengeRoutineCopyToParticipantData.getChallengeRoutineId()).get();
            MemberRoutine memberRoutine = createMemberRoutine(member, challengeRoutineCopyToParticipantData);

            for (ChallengeRoutineContent challengeRoutineContent : challengeRoutine.getChallengeRoutineContentList()) {

                MemberRoutineContent memberRoutineContent = createMemberRoutineContent(memberRoutine, challengeRoutineContent);
                log.info("asdasd = {}",memberRoutineContent.getChallengeRoutineContent());
                memberRoutine.getMemberRoutineContentList().add(memberRoutineContent);
            }

            memberRoutineRepository.save(memberRoutine);
        }

        challenge.setParticipationNum(challenge.getParticipationNum() + 1);

        ChallengeParticipant challengeParticipant = createChallengeParticipant(member, challenge);
        challengeParticipantRepository.save(challengeParticipant);
    }

    public Map<Challenge, Integer> findChallengeAndCompletedRoutinesNumMapByMemberId(Long memberId) {

        Member member = memberRepository.findById(memberId).get();
        List<ChallengeParticipant> challengeParticipantList = challengeParticipantRepository.findByMember(member);
        Map<Challenge, Integer> challengeAndCompletedRoutinesNumMap = new LinkedHashMap<>();

        for (ChallengeParticipant challengeParticipant : challengeParticipantList) {

            Challenge challenge = challengeParticipant.getChallenge();
            challengeAndCompletedRoutinesNumMap.put(challenge, challengeParticipant.getCompletedRoutinesNum());
        }

        return challengeAndCompletedRoutinesNumMap;
    }

    @Transactional
    public Long challengeAuthPost(Long challengeRoutineId, Long memberRoutineId, MultipartFile challengeAuthPostPhoto) {

        ChallengeRoutine challengeRoutine = challengeRoutineRepository.findById(challengeRoutineId).get();
        MemberRoutine memberRoutine = memberRoutineRepository.findById(memberRoutineId).get();
        Member member = memberRoutine.getMember();

        if(!validateAuthDate(memberRoutine.getRoutineRegisterdate())){
            return -1L;
        }

        String challengeAuthPhotoUrl = fileUploadService.uploadImage(challengeAuthPostPhoto);

        ChallengeAuthPost challengeAuthPost = createChallengeAuthPost(challengeRoutine, member, challengeAuthPhotoUrl);
        Long savedChallengeAuthPostId = challengeAuthPostRepository.save(challengeAuthPost).getId();
        ChallengeParticipant challengeParticipant = challengeParticipantRepository.findByChallengeAndMember(challengeRoutine.getChallenge(), member);

        challengeParticipant.setCompletedRoutinesNum(challengeParticipant.getCompletedRoutinesNum() + 1);

        return savedChallengeAuthPostId;
    }

    private boolean validateAuthDate(String routineRegisterdate) {
        return LocalDate.parse(routineRegisterdate, DateTimeFormatter.ISO_DATE).isEqual(LocalDate.now());
    }

    public List<ChallengeAuthPost> findAllChallengeAuthPost(Long challengeId) {

        Challenge challenge = challengeRepository.findById(challengeId).get();
        List<Long> challengeRoutineIdList = new ArrayList<>();

        for (ChallengeRoutine challengeRoutine : challenge.getChallengeRoutineList()) {
            challengeRoutineIdList.add(challengeRoutine.getId());
        }

        return challengeAuthPostRepository.findAllById(challengeRoutineIdList);
    }

    private ChallengeAuthPost createChallengeAuthPost(ChallengeRoutine challengeRoutine, Member member, String challengeAuthPhotoUrl) {
        return ChallengeAuthPost.builder()
                .challengeRoutine(challengeRoutine)
                .member(member)
                .photoUrl(challengeAuthPhotoUrl)
                .build();
    }

    private boolean dateValidaton(LocalDate currentDate) {
        log.info("localdate - now : {}", LocalDateTime.now());
        return currentDate.isEqual(LocalDate.now());
    }

    private ChallengeParticipant createChallengeParticipant(Member member, Challenge challenge) {
        return ChallengeParticipant.builder()
                .challenge(challenge)
                .member(member)
                .build();
    }

    private MemberRoutine createMemberRoutine(Member member, ChallengeRoutineCopyToParticipantData challengeRoutineCopyToParticipantData) {
        ChallengeRoutine challengeRoutine = challengeRoutineRepository.findById(challengeRoutineCopyToParticipantData.getChallengeRoutineId()).get();

        return MemberRoutine.builder()
                .member(member)
                .routineRegisterdate(challengeRoutineCopyToParticipantData.getChallengeRoutineProgressDate())
                .memberRoutineContentList(new ArrayList<>())
                .challengeRoutine(challengeRoutine)
                .build();
    }

    private MemberRoutineContent createMemberRoutineContent(MemberRoutine memberRoutine, ChallengeRoutineContent challengeRoutineContent) {
        return MemberRoutineContent.builder()
                .workout(challengeRoutineContent.getWorkout())
                .memberRoutineWorkoutSet(challengeRoutineContent.getChallengeRoutineWorkoutSet())
                .memberRoutineWorkoutCount(challengeRoutineContent.getChallengeRoutineWorkoutCount())
                .memberRoutineWorkoutTime(challengeRoutineContent.getChallengeRoutineWorkoutTime())
                .memberRoutineWorkoutWeight(challengeRoutineContent.getChallengeRoutineWorkoutWeight())
                .memberRoutine(memberRoutine)
                .challengeRoutineContent(challengeRoutineContent)
                .build();
    }

    public void updateExceptChallengeRoutineList(Challenge prevChallenge, Challenge challenge) {

        prevChallenge.setName(challenge.getName());
        prevChallenge.setStartDate(challenge.getStartDate());
        prevChallenge.setEndDate(challenge.getEndDate());
        prevChallenge.setParticipationNum(challenge.getParticipationNum());
        prevChallenge.setParticipationFee(challenge.getParticipationFee());
        prevChallenge.setNumPerWeek(challenge.getNumPerWeek());
    }

    private void clearRelation(Challenge prevChallenge) {
        for (ChallengeRoutine challengeRoutine : prevChallenge.getChallengeRoutineList()) {

            for (ChallengeRoutineContent challengeRoutineContent : challengeRoutine.getChallengeRoutineContentList()) {
                challengeRoutineContent.setChallengeRoutine(null);
            }

            challengeRoutine.getChallengeRoutineContentList().clear();
        }

        prevChallenge.getChallengeRoutineList().clear();
    }
}
