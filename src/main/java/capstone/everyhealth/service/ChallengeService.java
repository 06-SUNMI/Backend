package capstone.everyhealth.service;

import capstone.everyhealth.domain.challenge.Challenge;
import capstone.everyhealth.domain.challenge.ChallengeRoutine;
import capstone.everyhealth.domain.challenge.ChallengeRoutineContent;
import capstone.everyhealth.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

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

    @Transactional
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

    @Transactional
    public void delete(Long challengeId) {
        challengeRepository.deleteById(challengeId);
    }

    public void participate(Long memberId, Long challengeId) {
    }
}
