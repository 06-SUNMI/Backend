package capstone.everyhealth.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import capstone.everyhealth.controller.dto.Sns.SnsCommentUpdateRequset;
import capstone.everyhealth.domain.report.SnsCommentReport;
import capstone.everyhealth.domain.report.SnsPostReport;
import capstone.everyhealth.domain.sns.SnsPostImageOrVideo;
import capstone.everyhealth.domain.sns.SnsPostLikes;
import capstone.everyhealth.exception.Sns.SnsCommentNotFound;
import capstone.everyhealth.exception.Sns.SnsFollowingNotFound;
import capstone.everyhealth.exception.Sns.SnsPostNotFound;
import capstone.everyhealth.exception.report.DuplicateReporter;
import capstone.everyhealth.exception.report.WriterEqualsReporter;
import capstone.everyhealth.exception.stakeholder.MemberNotFound;
import capstone.everyhealth.fileupload.service.FileUploadService;
import capstone.everyhealth.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import capstone.everyhealth.domain.sns.SnsComment;
import capstone.everyhealth.domain.sns.SnsFollowing;
import capstone.everyhealth.domain.sns.SnsPost;
import capstone.everyhealth.domain.stakeholder.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j

public class SnsService {

    private final MemberRepository memberRepository;
    private final SnsRepository snsRepository;
    private final SnsCommentRepository snsCommentRepository;
    private final FileUploadService fileUploadService;
    private final SnsPostReportRepository snsPostReportRepository;
    private final SnsCommentReportRepository snsCommentReportRepository;
    private final SnsPostLikesRepository snsPostLikesRepository;

    public List<Member> findByName(String userName) {
        return memberRepository.findAllByName(userName);

    }

    @Transactional
    public Long save(String snsPostContent, Long userId, List<MultipartFile> snsPostsImageOrVideoFileList)
            throws MemberNotFound {

        Member member = memberService.findMemberById(userId);
        SnsPost snsPost = SnsPost.builder()
                .content(snsPostContent)
                .member(member)
                .build();

        if (snsPostsImageOrVideoFileList != null) {
            addSnsPostsOrVideoFileListInSnsPost(snsPostsImageOrVideoFileList, snsPost);
        }

        return snsRepository.save(snsPost).getId();
    }

    @Transactional
    public void update(Long snsPostId, String snsPostContent, List<MultipartFile> snsPostsImageOrVideoFileList)
            throws SnsPostNotFound {

        SnsPost originSnsPost = snsRepository.findById(snsPostId).orElseThrow(() -> new SnsPostNotFound(snsPostId));

        originSnsPost.setContent(snsPostContent);
        originSnsPost.getSnsPostImageOrVideoList().clear();

        if (snsPostsImageOrVideoFileList != null) {
            addSnsPostsOrVideoFileListInSnsPost(snsPostsImageOrVideoFileList, originSnsPost);
        }
    }

    public SnsPost findOne(Long snsPostId) throws SnsPostNotFound {
        return snsRepository.findById(snsPostId).orElseThrow(() -> new SnsPostNotFound(snsPostId));
    }

    public List<SnsPost> findAllSnsPosts() {
        return snsRepository.findAll();
    }

    @Autowired
    SnsFollowRepository snsFollowRepository;
    @Autowired
    MemberService memberService;

    @Transactional
    public Long follow(Long followedMemberId, Long followingMemberId) throws MemberNotFound, SnsFollowingNotFound { // 저장

        Member followedMember = memberRepository.findById(followedMemberId).orElseThrow(() -> new MemberNotFound(followedMemberId));
        Member followingMember = memberRepository.findById(followingMemberId).orElseThrow(() -> new MemberNotFound(followingMemberId));
        try {
            snsFollowRepository.findByFollowedMemberAndFollowingMember(followedMember, followingMember).orElseThrow(() -> new SnsFollowingNotFound(followedMemberId, followedMemberId));
        } catch (SnsFollowingNotFound e) {
            SnsFollowing snsFollowing = new SnsFollowing(); // 객체생성
            snsFollowing.setFollowedMember(memberService.findMemberById(followedMemberId));
            snsFollowing.setFollowingMember(memberService.findMemberById(followingMemberId)); // 필드에 값채우기
            return snsFollowRepository.save(snsFollowing).getId();// 저장
        }
        return -1L;
    }

    @Transactional
    public void unFollow(Long followedMemberId, Long followingMemberId) throws MemberNotFound, SnsFollowingNotFound {

        Member followedMember = memberRepository.findById(followedMemberId).orElseThrow(() -> new MemberNotFound(followedMemberId));
        Member followingMember = memberRepository.findById(followingMemberId).orElseThrow(() -> new MemberNotFound(followingMemberId));
        snsFollowRepository.findByFollowedMemberAndFollowingMember(followedMember, followingMember).orElseThrow(() -> new SnsFollowingNotFound(followedMember.getId(), followingMember.getId()));

        snsFollowRepository.deleteByFollowedMemberAndFollowingMember(followedMember, followingMember);
    }

    @Transactional
    public int postLike(Long snsPostId, Long memeberId) throws SnsPostNotFound, MemberNotFound {

        SnsPost snsPost = snsRepository.findById(snsPostId).get();
        Member member = memberRepository.findById(memeberId).get();
        Optional<SnsPostLikes> snsPostLikes = snsPostLikesRepository.findBySnsPostAndMember(snsPost, member);
        SnsPostLikes snsPostLikes2 = new SnsPostLikes();
        snsPostLikes2.setMember(member);
        snsPostLikes2.setSnsPost(snsPost);
        if (snsPostLikes.isEmpty()) {

            snsPost.setLikes(snsPost.getLikes() + 1);
            snsPostLikesRepository.save(snsPostLikes2);
            return snsPost.getLikes();

        }

        snsPost.setLikes(snsPost.getLikes() - 1);
        snsPostLikesRepository.deleteById(snsPostLikes.get().getId());
        return snsPost.getLikes();

    }


    @Transactional
    public Long saveComment(SnsComment snsComment) {
        return snsCommentRepository.save(snsComment).getId();
    }

    public List<SnsComment> findAllComment(Long snsPostId) throws SnsPostNotFound {
        SnsPost snsPost = snsRepository.findById(snsPostId).orElseThrow(() -> new SnsPostNotFound(snsPostId));
        return snsCommentRepository.findAllBySnsPost(snsPost);
    }

    @Transactional
    public void deletePost(Long snsId) {
        snsRepository.deleteById(snsId);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    @Transactional
    public Long reportSnsPost(Long snsPostId, Long memberId, String reportReason)
            throws SnsPostNotFound, MemberNotFound, DuplicateReporter, WriterEqualsReporter {

        SnsPost snsPost = snsRepository.findById(snsPostId).orElseThrow(() -> new SnsPostNotFound(snsPostId));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFound(memberId));

        validateSnsPostReport(snsPost, member);

        SnsPostReport snsPostReport = createSnsPostReport(snsPostId, memberId, reportReason);
        return snsPostReportRepository.save(snsPostReport).getId();
    }

    @Transactional
    public Long reportSnsComment(Long snsCommentId, Long memberId, String reportReason)
            throws SnsCommentNotFound, MemberNotFound, DuplicateReporter, WriterEqualsReporter {

        SnsComment snsComment = snsCommentRepository.findById(snsCommentId)
                .orElseThrow(() -> new SnsCommentNotFound(snsCommentId));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFound(memberId));

        validateSnsCommentReport(snsComment, member);

        SnsCommentReport snsCommentReport = createSnsCommentReport(snsCommentId, memberId, reportReason);
        return snsCommentReportRepository.save(snsCommentReport).getId();
    }

    @Transactional
    public void deleteComment(Long commentId) {
        snsCommentRepository.deleteById(commentId);
    }

    @Transactional // 수정
    public void updateComment(SnsCommentUpdateRequset snsCommentUpdateRequset, Long commentId) {
        SnsComment originComment = snsCommentRepository.findById(commentId).get();
        originComment.setSnsComment(snsCommentUpdateRequset.getSnsComment());
    }

    public List<Long> findFollowingMembers(Long memberId) throws MemberNotFound {

        List<Long> followedMemberIdList = new ArrayList<>();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFound(memberId));
        List<SnsFollowing> snsFollowingList = snsFollowRepository.findByFollowingMember(member);

        for (SnsFollowing snsFollowing : snsFollowingList) {
            followedMemberIdList.add(snsFollowing.getFollowedMember().getId());
        }

        return followedMemberIdList;
    }

    private void validateSnsCommentReport(SnsComment snsComment, Member member)
            throws WriterEqualsReporter, DuplicateReporter {
        if (snsComment.getMember().getId() == member.getId()) {
            throw new WriterEqualsReporter();
        }
        if (snsCommentReportRepository.findByMemberAndSnsComment(member, snsComment).isPresent()) {
            throw new DuplicateReporter();
        }
    }

    private void validateSnsPostReport(SnsPost snsPost, Member member) throws WriterEqualsReporter, DuplicateReporter {
        if (snsPost.getMember().getId() == member.getId()) {
            throw new WriterEqualsReporter();
        }
        if (snsPostReportRepository.findByMemberAndSnsPost(member, snsPost).isPresent()) {
            throw new DuplicateReporter();
        }
    }

    private SnsCommentReport createSnsCommentReport(Long snsCommentId, Long memberId, String reportReason)
            throws SnsCommentNotFound, MemberNotFound {
        return SnsCommentReport.builder()
                .snsComment(snsCommentRepository.findById(snsCommentId)
                        .orElseThrow(() -> new SnsCommentNotFound(snsCommentId)))
                .member(memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFound(memberId)))
                .reason(reportReason)
                .registeredDate(LocalDate.now())
                .build();
    }

    private SnsPostReport createSnsPostReport(Long snsPostId, Long memberId, String reportReason)
            throws SnsPostNotFound, MemberNotFound {
        return SnsPostReport.builder()
                .snsPost(snsRepository.findById(snsPostId).orElseThrow(() -> new SnsPostNotFound(snsPostId)))
                .member(memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFound(memberId)))
                .reason(reportReason)
                .registeredDate(LocalDate.now())
                .build();
    }

    private void addSnsPostsOrVideoFileListInSnsPost(List<MultipartFile> snsPostsImageOrVideoFileList,
                                                     SnsPost snsPost) {
        for (MultipartFile imageOrVideoFile : snsPostsImageOrVideoFileList) {

            String imageOrVideoFileUrl = fileUploadService.uploadImage(imageOrVideoFile);
            SnsPostImageOrVideo snsPostImageOrVideo = SnsPostImageOrVideo.builder()
                    .imageOrVideoUrl(imageOrVideoFileUrl)
                    .snsPost(snsPost)
                    .build();

            snsPost.addImageOrVideo(snsPostImageOrVideo);
        }
    }
}
