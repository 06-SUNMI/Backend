package capstone.everyhealth.service;

import capstone.everyhealth.controller.dto.Challenge.ChallengeRoutineCopyToParticipantData;
import capstone.everyhealth.controller.dto.Challenge.ChallengeRoutineCopyToParticipantRequest;
import capstone.everyhealth.domain.challenge.Challenge;
import capstone.everyhealth.domain.challenge.ChallengeParticipant;
import capstone.everyhealth.domain.challenge.ChallengeRoutine;
import capstone.everyhealth.domain.challenge.ChallengeRoutineContent;
import capstone.everyhealth.domain.routine.MemberRoutine;
import capstone.everyhealth.domain.routine.MemberRoutineContent;
import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.ArrayList;
import java.util.List;

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

            ChallengeRoutine challengeRoutine = challengeRoutineRepository.findById(challengeRoutineCopyToParticipantData.getRoutineId()).get();
            MemberRoutine memberRoutine = createMemberRoutine(member, challengeRoutineCopyToParticipantData);

            for (ChallengeRoutineContent challengeRoutineContent : challengeRoutine.getChallengeRoutineContentList()) {

                MemberRoutineContent memberRoutineContent = createMemberRoutineContent(memberRoutine, challengeRoutineContent);
                memberRoutine.getMemberRoutineContentList().add(memberRoutineContent);
            }

            memberRoutineRepository.save(memberRoutine);
        }

        challenge.setParticipationNum(challenge.getParticipationNum() + 1);

        ChallengeParticipant challengeParticipant = createChallengeParticipant(member, challenge);
        challengeParticipantRepository.save(challengeParticipant);
    }

    private ChallengeParticipant createChallengeParticipant(Member member, Challenge challenge) {
        return ChallengeParticipant.builder()
                .challenge(challenge)
                .member(member)
                .build();
    }

    private MemberRoutine createMemberRoutine(Member member, ChallengeRoutineCopyToParticipantData challengeRoutineCopyToParticipantData) {
        return MemberRoutine.builder()
                .member(member)
                .routineRegisterdate(challengeRoutineCopyToParticipantData.getChallengeRoutineProgressDate())
                .memberRoutineContentList(new ArrayList<>())
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
                .build();
    }

    public void updateExceptChallengeRoutineList(Challenge prevChallenge, Challenge challenge) {

        prevChallenge.setName(challenge.getName());
        prevChallenge.setStartDate(challenge.getStartDate());
        prevChallenge.setEndDate(challenge.getEndDate());
        prevChallenge.setParticipationNum(challenge.getParticipationNum());
        prevChallenge.setParticipationFee(challenge.getParticipationFee());
        prevChallenge.setPreparations(challenge.getPreparations());
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
