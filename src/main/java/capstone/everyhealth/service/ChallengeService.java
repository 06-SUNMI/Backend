package capstone.everyhealth.service;

import capstone.everyhealth.controller.dto.Challenge.payment.ChallengeTransactionHistory;
import capstone.everyhealth.domain.challenge.*;
import capstone.everyhealth.domain.enums.ChallengePaymentStatus;
import capstone.everyhealth.domain.enums.ChallengeStatus;
import capstone.everyhealth.domain.routine.MemberRoutine;
import capstone.everyhealth.domain.routine.MemberRoutineContent;
import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.exception.challenge.*;
import capstone.everyhealth.exception.memberroutine.MemberRoutineNotFound;
import capstone.everyhealth.exception.stakeholder.MemberNotFound;
import capstone.everyhealth.fileupload.service.FileUploadService;
import capstone.everyhealth.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
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
    private final ChallengeTransactionRepository challengeTransactionRepository;

    @Transactional
    public Long save(Challenge challenge) {
        return challengeRepository.save(challenge).getId();
    }

    public List<Challenge> findAll() {
        return challengeRepository.findAll();
    }

    public Challenge find(Long challengeId) throws ChallengeNotFound {
        return challengeRepository.findById(challengeId).orElseThrow(() -> new ChallengeNotFound(challengeId));
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
    public int participate(Long memberId, Long challengeId, List<String> challengeRoutineProgressDateList) throws MemberNotFound, ChallengeNotFound, NotInChallengeRoutineProgressDateRange, SelectedDatesNumNotEqualsWithChallenge, DuplicateChallengeParticipant {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFound(memberId));
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new ChallengeNotFound(challengeId));

        validateChallengeParticipation(challengeRoutineProgressDateList, challenge, member);

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

    public List<ChallengeParticipant> findChallengeParticipantListByMemberId(Long memberId) throws MemberNotFound {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFound(memberId));
        List<ChallengeParticipant> challengeParticipantList = challengeParticipantRepository.findByMember(member);

        return challengeParticipantList;
    }

    @Transactional
    public Long challengeAuthPost(Long challengeRoutineId, Long memberRoutineId, MultipartFile challengeAuthPostPhoto) throws ChallengeParticipantNotFound, ChallengeRoutineNotFound, MemberRoutineNotFound, DuplicateChallengeAuthInRoutine, NotAllRoutineContentsProgressedInChallenge {

        ChallengeRoutine challengeRoutine = challengeRoutineRepository.findById(challengeRoutineId).orElseThrow(() -> new ChallengeRoutineNotFound(challengeRoutineId));
        MemberRoutine memberRoutine = memberRoutineRepository.findById(memberRoutineId).orElseThrow(() -> new MemberRoutineNotFound(memberRoutineId));
        Member member = memberRoutine.getMember();

        validateChallengeAuthPost(member, challengeRoutine, memberRoutine);

        String challengeAuthPhotoUrl = fileUploadService.uploadImage(challengeAuthPostPhoto);

        ChallengeAuthPost challengeAuthPost = createChallengeAuthPost(challengeRoutine, member, challengeAuthPhotoUrl);
        Long savedChallengeAuthPostId = challengeAuthPostRepository.save(challengeAuthPost).getId();
        ChallengeParticipant challengeParticipant = challengeParticipantRepository.findByChallengeAndMember(challengeRoutine.getChallenge(), member).orElseThrow(() -> new ChallengeParticipantNotFound());

        challengeParticipant.setCompletedRoutinesNum(challengeParticipant.getCompletedRoutinesNum() + 1);

        return savedChallengeAuthPostId;
    }

    @Transactional
    public Long challengeAuthPostForTest(Long challengeRoutineId, Long memberRoutineId) throws ChallengeParticipantNotFound, ChallengeRoutineNotFound, MemberRoutineNotFound, DuplicateChallengeAuthInRoutine, NotAllRoutineContentsProgressedInChallenge {

        ChallengeRoutine challengeRoutine = challengeRoutineRepository.findById(challengeRoutineId).orElseThrow(() -> new ChallengeRoutineNotFound(challengeRoutineId));
        MemberRoutine memberRoutine = memberRoutineRepository.findById(memberRoutineId).orElseThrow(() -> new MemberRoutineNotFound(memberRoutineId));
        Member member = memberRoutine.getMember();

        validateChallengeAuthPost(member, challengeRoutine, memberRoutine);

        ChallengeAuthPost challengeAuthPost = createChallengeAuthPostForTest(challengeRoutine, member);
        Long savedChallengeAuthPostId = challengeAuthPostRepository.save(challengeAuthPost).getId();
        ChallengeParticipant challengeParticipant = challengeParticipantRepository.findByChallengeAndMember(challengeRoutine.getChallenge(), member).orElseThrow(() -> new ChallengeParticipantNotFound());

        challengeParticipant.setCompletedRoutinesNum(challengeParticipant.getCompletedRoutinesNum() + 1);

        return savedChallengeAuthPostId;
    }

    public List<ChallengeAuthPost> findAllChallengeAuthPost(Long challengeId) throws ChallengeNotFound {

        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new ChallengeNotFound(challengeId));
        List<ChallengeAuthPost> totalChallengeAuthPostList = challengeAuthPostRepository.findAll();
        List<ChallengeAuthPost> returnChallengeAuthPostList = new ArrayList<>();

        for (ChallengeAuthPost challengeAuthPost : totalChallengeAuthPostList) {

            if (challengeAuthPost.getChallengeRoutine().getChallenge() == challenge) {
                returnChallengeAuthPostList.add(challengeAuthPost);
            }
        }

        return returnChallengeAuthPostList;
    }

    @Transactional
    public void updateChallengeStatusFinished(Long challengeId) throws ChallengeNotFound {

        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new ChallengeNotFound(challengeId));
        challenge.setFinished(true);
    }

    public List<ChallengeParticipant> findChallengeParticipantListByChallenge(Challenge challenge) {
        return challengeParticipantRepository.findAllByChallenge(challenge);
    }

    @Transactional
    public void updateChallengeParticipantStatus(Long challengeParticipantId, ChallengeStatus challengeStatus) throws ChallengeParticipantNotFound {

        ChallengeParticipant challengeParticipant = challengeParticipantRepository.findById(challengeParticipantId).orElseThrow(() -> new ChallengeParticipantNotFound());
        challengeParticipant.setChallengeStatus(challengeStatus);
    }

    @Transactional
    public void saveChallengePaymentResult(JSONObject paymentData, Long challengeId, Long memberId) throws ChallengeNotFound, MemberNotFound, ChallengeParticipantNotFound {

        ChallengeParticipant challengeParticipant = findChallengeParticipant(challengeId,memberId);
        ChallengeTransaction challengeTransaction = createChallengeTransaction(paymentData, challengeParticipant);
        challengeTransaction.addTransaction(challengeParticipant);
        log.info("challengeParticipant = {}",challengeParticipant);
        challengeTransactionRepository.save(challengeTransaction);
    }

    private ChallengeTransaction createChallengeTransaction(JSONObject paymentData, ChallengeParticipant challengeParticipant) {
        return ChallengeTransaction.builder()
                .challengePaymentStatus(ChallengePaymentStatus.PAID)
                .challengeParticipant(challengeParticipant)
                .merchantUid((String) paymentData.get("merchant_uid"))
                .build();
    }

    public ChallengeParticipant findChallengeParticipant(Long challengeId, Long memberId) throws ChallengeNotFound, MemberNotFound, ChallengeParticipantNotFound {

        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new ChallengeNotFound(challengeId));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFound(memberId));

        return challengeParticipantRepository.findByChallengeAndMember(challenge, member).orElseThrow(() -> new ChallengeParticipantNotFound());
    }

    @Transactional
    public void refund(ChallengeTransaction challengeTransaction) {
        challengeTransaction.setChallengePaymentStatus(ChallengePaymentStatus.REFUNDED);
    }

    public List<ChallengeTransaction> findChallengeTransactionHistory(Long memberId) throws MemberNotFound, ChallengeNotFound, ChallengeParticipantNotFound {

        Member member = memberRepository.findById(memberId).orElseThrow(()->new MemberNotFound(memberId));
        List<ChallengeParticipant> challengeParticipantList = challengeParticipantRepository.findAllByMember(member);
        List<ChallengeTransaction> challengeTransactionList = challengeTransactionRepository.findAllByChallengeParticipantIn(challengeParticipantList);

        return challengeTransactionList;
    }

    private void validateChallengeAuthPost(Member member, ChallengeRoutine challengeRoutine, MemberRoutine memberRoutine) throws NotAllRoutineContentsProgressedInChallenge, DuplicateChallengeAuthInRoutine {

        if (!validateIsAllRoutineContentsProgressed(memberRoutine)) {
            throw new NotAllRoutineContentsProgressedInChallenge();
        }

        if (!validateIsDuplicateParticipation(member, challengeRoutine)) {
            throw new DuplicateChallengeAuthInRoutine();
        }
    }

    private boolean validateIsRenderedFileExist(MultipartFile challengeAuthPostPhoto) {
        return challengeAuthPostPhoto != null;
    }

    private boolean validateIsAllRoutineContentsProgressed(MemberRoutine memberRoutine) {
        log.info("memberRoutine.getProgressRate() = {}", memberRoutine.getProgressRate());
        return memberRoutine.getProgressRate() == 100;
    }

    private boolean validateIsDuplicateParticipation(Member member, ChallengeRoutine challengeRoutine) {
        ChallengeAuthPost challengeAuthPost = challengeAuthPostRepository.findByMemberAndChallengeRoutine(member, challengeRoutine);

        return challengeAuthPost == null;
    }

    private void validateChallengeParticipation(List<String> challengeRoutineProgressDateList, Challenge challenge, Member member) throws SelectedDatesNumNotEqualsWithChallenge, NotInChallengeRoutineProgressDateRange, DuplicateChallengeParticipant {

        if (!challengeParticipantRepository.findByChallengeAndMember(challenge, member).isEmpty()) {
            throw new DuplicateChallengeParticipant();
        }

        if (!validateChallengeRoutineProgressDateNum(challenge, challengeRoutineProgressDateList)) {
            throw new SelectedDatesNumNotEqualsWithChallenge();
        }
        if (!validateChallengeRoutineProgressDateRange(challenge, challengeRoutineProgressDateList)) {
            throw new NotInChallengeRoutineProgressDateRange();
        }
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

            if ((count >= challenge.getNumPerWeek())) {
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

    private ChallengeAuthPost createChallengeAuthPostForTest(ChallengeRoutine challengeRoutine, Member member) {
        return ChallengeAuthPost.builder()
                .challengeRoutine(challengeRoutine)
                .member(member)
                .photoUrl(null)
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
