package capstone.everyhealth.service;

import java.util.List;

import capstone.everyhealth.domain.report.SnsCommentReport;
import capstone.everyhealth.domain.report.SnsPostReport;
import capstone.everyhealth.domain.sns.SnsPostImageOrVideo;
import capstone.everyhealth.exception.Sns.SnsCommentNotFound;
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

    public List<Member> findByName(String userName) {
        return memberRepository.findAllByName(userName);

    }

    @Transactional
    public Long save(String snsPostContent, Long userId, List<MultipartFile> snsPostsImageOrVideoFileList) throws MemberNotFound {

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
    public void update(Long snsPostId, String snsPostContent, List<MultipartFile> snsPostsImageOrVideoFileList) throws SnsPostNotFound {

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
    public Long follow(Long followedMemberId, Long followingMemberId) throws MemberNotFound { // 저장
        SnsFollowing snsFollowing = new SnsFollowing(); // 객체생성
        snsFollowing.setFollowedMember(memberService.findMemberById(followedMemberId));
        snsFollowing.setFollowingMember(memberService.findMemberById(followingMemberId)); // 필드에 값채우기
        return snsFollowRepository.save(snsFollowing).getId();// 저장
    }

    @Transactional
    public void unFollow(Long followedMemberId, Long followingMemberId) throws MemberNotFound {
        Member followedMember = memberService.findMemberById(followedMemberId);
        Member followingMember = memberService.findMemberById(followingMemberId);
        snsFollowRepository.deleteByFollowedMemberAndFollowingMember(followedMember, followingMember);
    }

    @Transactional
    public int addLike(Long snsId) throws SnsPostNotFound {
        SnsPost snsPost = snsRepository.findById(snsId).orElseThrow(() -> new SnsPostNotFound(snsId));
        snsPost.setLikes(snsPost.getLikes() + 1);
        return snsPost.getLikes();
    }

    @Transactional
    public int cancelLike(Long snsId) throws SnsPostNotFound {
        SnsPost snsPost = snsRepository.findById(snsId).orElseThrow(() -> new SnsPostNotFound(snsId));
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

    @Transactional
    public Long reportSnsPost(Long snsPostId, Long memberId, String reportReason) throws SnsPostNotFound, MemberNotFound, DuplicateReporter, WriterEqualsReporter {

        SnsPost snsPost = snsRepository.findById(snsPostId).orElseThrow(() -> new SnsPostNotFound(snsPostId));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFound(memberId));

        validateSnsPostReport(snsPost, member);

        SnsPostReport snsPostReport = createSnsPostReport(snsPostId, memberId, reportReason);
        return snsPostReportRepository.save(snsPostReport).getId();
    }

    @Transactional
    public Long reportSnsComment(Long snsCommentId, Long memberId, String reportReason) throws SnsCommentNotFound, MemberNotFound, DuplicateReporter, WriterEqualsReporter {

        SnsComment snsComment = snsCommentRepository.findById(snsCommentId).orElseThrow(() -> new SnsCommentNotFound(snsCommentId));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFound(memberId));

        validateSnsCommentReport(snsComment, member);

        SnsCommentReport snsCommentReport = createSnsCommentReport(snsCommentId, memberId, reportReason);
        return snsCommentReportRepository.save(snsCommentReport).getId();
    }

    private void validateSnsCommentReport(SnsComment snsComment, Member member) throws WriterEqualsReporter, DuplicateReporter {
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

    private SnsCommentReport createSnsCommentReport(Long snsCommentId, Long memberId, String reportReason) throws SnsCommentNotFound, MemberNotFound {
        return SnsCommentReport.builder()
                .snsComment(snsCommentRepository.findById(snsCommentId).orElseThrow(() -> new SnsCommentNotFound(snsCommentId)))
                .member(memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFound(memberId)))
                .reason(reportReason)
                .build();
    }

    private SnsPostReport createSnsPostReport(Long snsPostId, Long memberId, String reportReason) throws SnsPostNotFound, MemberNotFound {
        return SnsPostReport.builder()
                .snsPost(snsRepository.findById(snsPostId).orElseThrow(() -> new SnsPostNotFound(snsPostId)))
                .member(memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFound(memberId)))
                .reason(reportReason)
                .build();
    }

    private void addSnsPostsOrVideoFileListInSnsPost(List<MultipartFile> snsPostsImageOrVideoFileList, SnsPost snsPost) {
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
