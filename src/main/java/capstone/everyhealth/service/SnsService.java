package capstone.everyhealth.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import capstone.everyhealth.controller.dto.SnsUpdateRequest;
import capstone.everyhealth.domain.sns.SnsPost;
import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.repository.MemberRepository;
import capstone.everyhealth.repository.SnsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j

public class SnsService {

    private final MemberRepository memberRepository;
    private final SnsRepository snsRepository;

    public List<Member> findByName(String userName) {
        return memberRepository.findAllByName(userName);

    }

    @Transactional
    public Long save(SnsPost snsPost) {

        return snsRepository.save(snsPost).getId();
    }

    public SnsPost findOne(Long snsId) {
        return snsRepository.findById(snsId).get();
    }

    @Transactional
    public void update(SnsUpdateRequest snsUpdateRequest, Long snsId) {
        SnsPost originSnsPost = snsRepository.findById(snsId).get();

        originSnsPost.setContent(snsUpdateRequest.getSnsContent());
        originSnsPost.setVideoLink(snsUpdateRequest.getSnsVideoLink());
        originSnsPost.setImageLink(snsUpdateRequest.getSnsImageLink());
    }

}