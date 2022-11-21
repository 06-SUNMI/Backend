package capstone.everyhealth.service;

import capstone.everyhealth.domain.challenge.*;
import capstone.everyhealth.domain.enums.ChallengeStatus;
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
    public Long delete(Long challengeId) {
        challengeRepository.deleteById(challengeId);
        return challengeId;
    }

    @Transactional
    public int participate(Long memberId, Long challengeId, List<String> challengeRoutineProgressDateList) {

        Member member = memberRepository.findById(memberId).get();
        Challenge challenge = challengeRepository.findById(challengeId).get();
        ChallengeParticipant challengeParticipant = challengeParticipantRepository.findByChallengeAndMember(challenge, member);
        int validationResult = validateChallengeParticipation(challengeRoutineProgressDateList, challenge, challengeParticipant);

        if (validationResult < 0) {
            return validationResult;
        }

        for (ChallengeRoutine challengeRoutine : challenge.getChallengeRoutineList()) {

            String challengeRoutineProgressDate = challengeRoutineProgressDateList.get(0);
            challengeRoutineProgressDateList.remove(0);
            MemberRoutine memberRoutine = createMemberRoutine(member, challengeRoutineProgressDate, challengeRoutine);

            for (ChallengeRoutineContent challengeRoutineContent : challengeRoutine.getChallengeRoutineContentList()) {

                MemberRoutineContent memberRoutineContent = createMemberRoutineContent(memberRoutine, challengeRoutineContent);
                memberRoutine.getMemberRoutineContentList().add(memberRoutineContent);
            }

            memberRoutineRepository.save(memberRoutine);
        }

        challenge.setParticipationNum(challenge.getParticipationNum() + 1);

        ChallengeParticipant newChallengeParticipant = createChallengeParticipant(member, challenge);
        log.info("member = {}", member);
        log.info("challenge = {}", challenge);
        log.info("newChallengeParticipant = {}", newChallengeParticipant);
        challengeParticipantRepository.save(newChallengeParticipant);

        return 1;
    }

    public List<ChallengeParticipant> findChallengeParticipantListByMemberId(Long memberId) {

        Member member = memberRepository.findById(memberId).get();
        List<ChallengeParticipant> challengeParticipantList = challengeParticipantRepository.findByMember(member);

        return challengeParticipantList;
        /*Member member = memberRepository.findById(memberId).get();
        List<ChallengeParticipant> challengeParticipantList = challengeParticipantRepository.findByMember(member);
        Map<Challenge, Integer> challengeAndCompletedRoutinesNumMap = new LinkedHashMap<>();

        for (ChallengeParticipant challengeParticipant : challengeParticipantList) {

            Challenge challenge = challengeParticipant.getChallenge();
            challengeAndCompletedRoutinesNumMap.put(challenge, challengeParticipant.getCompletedRoutinesNum());
        }

        return challengeAndCompletedRoutinesNumMap;*/
    }

    @Transactional
    public Long challengeAuthPost(Long challengeRoutineId, Long memberRoutineId, MultipartFile challengeAuthPostPhoto) {

        ChallengeRoutine challengeRoutine = challengeRoutineRepository.findById(challengeRoutineId).get();
        MemberRoutine memberRoutine = memberRoutineRepository.findById(memberRoutineId).get();
        Member member = memberRoutine.getMember();

        Long validationResult = validateChallengeAuthPost(member, challengeRoutine, memberRoutine, challengeAuthPostPhoto);

        if (validationResult.intValue() < 0) {
            return validationResult;
        }

        String challengeAuthPhotoUrl = fileUploadService.uploadImage(challengeAuthPostPhoto);

        ChallengeAuthPost challengeAuthPost = createChallengeAuthPost(challengeRoutine, member, challengeAuthPhotoUrl);
        Long savedChallengeAuthPostId = challengeAuthPostRepository.save(challengeAuthPost).getId();
        ChallengeParticipant challengeParticipant = challengeParticipantRepository.findByChallengeAndMember(challengeRoutine.getChallenge(), member);

        challengeParticipant.setCompletedRoutinesNum(challengeParticipant.getCompletedRoutinesNum() + 1);

        return savedChallengeAuthPostId;
    }

    public List<ChallengeAuthPost> findAllChallengeAuthPost(Long challengeId) {

        Challenge challenge = challengeRepository.findById(challengeId).get();
        List<ChallengeAuthPost> totalChallengeAuthPostList = challengeAuthPostRepository.findAll();
        List<ChallengeAuthPost> returnChallengeAuthPostList = new ArrayList<>();

        for (ChallengeAuthPost challengeAuthPost : totalChallengeAuthPostList) {

            if (challengeAuthPost.getChallengeRoutine().getChallenge() == challenge) {
                returnChallengeAuthPostList.add(challengeAuthPost);
            }
        }

        return returnChallengeAuthPostList;
    }

    private boolean validateAuthDate(String routineRegisterdate) {
        return LocalDate.parse(routineRegisterdate, DateTimeFormatter.ISO_DATE).isEqual(LocalDate.now());
    }

    public List<ChallengeParticipant> findChallengeParticipantListByChallenge(Challenge challenge) {
        return challengeParticipantRepository.findByChallenge(challenge);
    }

    private Long validateChallengeAuthPost(Member member, ChallengeRoutine challengeRoutine, MemberRoutine memberRoutine, MultipartFile challengeAuthPostPhoto) {

        if (!validateIsAllRoutineContentsProgressed(memberRoutine)){
            return -3L;
        }

        if (!validateIsDuplicateParticipation(member, challengeRoutine)) {
            return -2L;
        }

        if (!validateAuthDate(memberRoutine)) {
            return -1L;
        }

        return 1L;
    }

    private boolean validateIsRenderedFileExist(MultipartFile challengeAuthPostPhoto) {
        return challengeAuthPostPhoto != null;
    }

    private boolean validateIsAllRoutineContentsProgressed(MemberRoutine memberRoutine) {
        log.info("memberRoutine.getProgressRate() = {}",memberRoutine.getProgressRate());
        return memberRoutine.getProgressRate() == 100;
    }

    private boolean validateIsDuplicateParticipation(Member member, ChallengeRoutine challengeRoutine) {
        ChallengeAuthPost challengeAuthPost = challengeAuthPostRepository.findByMemberAndChallengeRoutine(member, challengeRoutine);

        return challengeAuthPost == null;
    }

    private Integer validateChallengeParticipation(List<String> challengeRoutineProgressDateList, Challenge challenge, ChallengeParticipant challengeParticipant) {
        if (challengeParticipant != null) {
            return -3;
        }
        if (!validateChallengeRoutineProgressDateNum(challenge, challengeRoutineProgressDateList)) {
            return -1;
        }
        if (!validateChallengeRoutineProgressDateRange(challenge, challengeRoutineProgressDateList)) {
            return -2;
        }
        return 1;
    }

    private boolean validateChallengeRoutineProgressDateNum(Challenge challenge, List<String> challengeRoutineProgressDateList) {

        log.info("challenge.getChallengeRoutineList().size() = {}", challenge.getChallengeRoutineList().size());
        log.info("challengeRoutineProgressDateList.size() = {}", challengeRoutineProgressDateList.size());

        return challenge.getChallengeRoutineList().size() == challengeRoutineProgressDateList.size();
    }

    private boolean validateChallengeRoutineProgressDateRange(Challenge challenge, List<String> challengeRoutineProgressDateList) {

        LocalDate startDate = LocalDate.parse(challenge.getStartDate(), DateTimeFormatter.ISO_DATE);
        int count = 0;

        for (String challengeRoutineProgressDateInString : challengeRoutineProgressDateList) {

            LocalDate challengeRoutineProgressDate = LocalDate.parse(challengeRoutineProgressDateInString, DateTimeFormatter.ISO_DATE);

            log.info("startDate = {}", startDate);
            log.info("startDate + 5 = {}", startDate.plusDays(5));
            log.info("startDate - 1 = {}", startDate.minusDays(1));
            log.info("challengeRoutineProgressDate = {}", challengeRoutineProgressDate);

            log.info("isAfter = {}", challengeRoutineProgressDate.isAfter(startDate.minusDays(1)));
            log.info("isBefore = {}", challengeRoutineProgressDate.isBefore(startDate.plusDays(7)));

            if (!((challengeRoutineProgressDate.isAfter(startDate.minusDays(1))
                    && (challengeRoutineProgressDate.isBefore(startDate.plusDays(7)))))) {
                return false;
            }
            count++;

            if ((count >= 2)) {
                startDate = startDate.plusDays(7);
                count = 0;
            }
        }
        return true;
    }

    private boolean validateAuthDate(MemberRoutine memberRoutine) {
        return LocalDate.parse(memberRoutine.getRoutineRegisterdate(), DateTimeFormatter.ISO_DATE).isEqual(LocalDate.now());
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
                .challengeStatus(ChallengeStatus.IN_PROGRESS)
                .build();
    }

    private MemberRoutine createMemberRoutine(Member member, String challengeRoutineProgressDate, ChallengeRoutine challengeRoutine) {
        return MemberRoutine.builder()
                .member(member)
                .routineRegisterdate(challengeRoutineProgressDate)
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
