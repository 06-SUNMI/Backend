package capstone.everyhealth.service;

import java.util.List;

import javax.xml.stream.events.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import capstone.everyhealth.controller.dto.Sns.SnsUpdateRequest;
import capstone.everyhealth.domain.sns.SnsComment;
import capstone.everyhealth.domain.sns.SnsFollowing;
import capstone.everyhealth.domain.sns.SnsPost;
import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.repository.MemberRepository;
import capstone.everyhealth.repository.SnsCommentRepository;
import capstone.everyhealth.repository.SnsFollowRepository;
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
    private final SnsCommentRepository snsCommentRepository;

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

    public List<SnsPost> findAll() {
        return snsRepository.findAll();
    }

    @Autowired
    SnsFollowRepository snsFollowRepository;
    @Autowired
    MemberService memberService;

    @Transactional
    public Long follow(Long followedMemberId, Long followingMemberId) { // 저장
        SnsFollowing snsFollowing = new SnsFollowing(); // 객체생성
        snsFollowing.setFollowedMember(memberService.findMemberById(followedMemberId));
        snsFollowing.setFollowingMember(memberService.findMemberById(followingMemberId)); // 필드에 값채우기
        return snsFollowRepository.save(snsFollowing).getId();// 저장
    }

    @Transactional
    public void unFollow(Long followedMemberId, Long followingMemberId) {
        Member followedMember = memberService.findMemberById(followedMemberId);
        Member followingMember = memberService.findMemberById(followingMemberId);
        snsFollowRepository.deleteByFollowedMemberAndFollowingMember(followedMember, followingMember);
    }

    @Transactional
    public int addLike(Long snsId) {
        SnsPost snsPost = snsRepository.findById(snsId).get();
        snsPost.setLikes(snsPost.getLikes() + 1);
        return snsPost.getLikes();
    }

    @Transactional
    public int cancelLike(Long snsId) {
        SnsPost snsPost = snsRepository.findById(snsId).get();
        snsPost.setLikes(snsPost.getLikes() - 1);
        return snsPost.getLikes();
    }

    @Transactional
    public Long saveComment(SnsComment snsComment) {
        return snsCommentRepository.save(snsComment).getId();
    }

    public List<SnsComment> findAllComment() {
        return snsCommentRepository.findAll();
    }

    @Transactional
    public void deletePost(Long snsId) {
        snsRepository.deleteById(snsId);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }
}
