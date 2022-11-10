package capstone.everyhealth.service;

import capstone.everyhealth.controller.dto.Challenge.ChallengePostOrUpdateRequest;
import capstone.everyhealth.domain.challenge.Challenge;
import capstone.everyhealth.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
    public Long update(ChallengePostOrUpdateRequest challengePostOrUpdateRequest, Long challengeId) {

        Challenge challenge = challengeRepository.findById(challengeId).get();

        challenge.updateContent(challengePostOrUpdateRequest);

        return challenge.getId();
    }

    @Transactional
    public void delete(Long challengeId) {
        challengeRepository.deleteById(challengeId);
    }

    public void participate(Long memberId, Long challengeId) {
    }
}
