package capstone.everyhealth.controller;

import java.util.ArrayList;
import java.util.List;


import javax.transaction.TransactionScoped;


import capstone.everyhealth.controller.dto.Sns.SnsCommentRequset;
import capstone.everyhealth.controller.dto.Sns.SnsCommentResponse;
import capstone.everyhealth.controller.dto.Sns.SnsFindResponse;

import capstone.everyhealth.controller.dto.Stakeholder.MemberFindByNameResponse;
import capstone.everyhealth.controller.dto.Stakeholder.MemberFindResponse;
import capstone.everyhealth.controller.dto.Stakeholder.MemberProfileFindResponse;
import capstone.everyhealth.domain.sns.SnsPostImageOrVideo;
import capstone.everyhealth.exception.Sns.SnsCommentNotFound;
import capstone.everyhealth.exception.Sns.SnsFollowingNotFound;
import capstone.everyhealth.exception.Sns.SnsPostNotFound;
import capstone.everyhealth.exception.report.DuplicateReporter;
import capstone.everyhealth.exception.report.WriterEqualsReporter;
import capstone.everyhealth.exception.stakeholder.MemberNotFound;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import capstone.everyhealth.domain.sns.SnsComment;
import capstone.everyhealth.domain.sns.SnsPost;
import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.service.MemberService;
import capstone.everyhealth.service.SnsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"SNS API"})
public class SnsController {

    private final SnsService snsService;
    private final MemberService memberService;

    @ApiOperation(
            value = "유저 이름 검색",
            notes = "검색한 이름과 일치하는 유저들을 조회한다."
    )
    @GetMapping("/sns/members/{memberName}")
    public List<MemberFindByNameResponse> search(@ApiParam(value = "검색할 유저 이름") @PathVariable String memberName) {

        List<Member> memberList = snsService.findByName(memberName);
        List<MemberFindByNameResponse> memberFindByNameResponseList = new ArrayList<>();

        for(Member member: memberList){
            MemberFindByNameResponse memberFindByNameResponse = MemberFindByNameResponse.builder()
                    .name(member.getName())
                    .gymName(member.getGymName())
                    .email(member.getEmail())
                    .gymId(member.getGymId())
                    .loginAt(member.getLoginAt())
                    .height(member.getHeight())
                    .customProfileImageUrl(member.getCustomProfileImageUrl())
                    .memberId(member.getId())
                    .point(member.getPoint())
                    .socialId(member.getSocialAccountId())
                    .weight(member.getWeight())
                    .build();

            memberFindByNameResponseList.add(memberFindByNameResponse);
        }

        return memberFindByNameResponseList;
    }

    @ApiOperation(
            value = "Sns 게시글 작성",
            notes = "유저가 작성한 Sns 게시글을 저장한다."
    )
    @PostMapping(path = "/sns/posts/members/{memberId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Long save(@ApiParam(value = "게시글 파일 (여러개 가능)") @RequestPart(required = false) List<MultipartFile> snsPostsImageOrVideoFileList,
                     @ApiParam(value = "사용자 id 값") @PathVariable Long memberId,
            /*@ApiParam(value = "작성 게시글의 내용") @RequestPart SnsPostRequest snsPostRequest)*/
                     @ApiParam(value = "Sns 작성 글 내용") @RequestPart String snsPostContent) throws MemberNotFound {
        return snsService.save(snsPostContent, memberId, snsPostsImageOrVideoFileList);
    }

    @ApiOperation(
            value = "Sns 게시글 불러오기",
            notes = "Sns 전체 게시글을 불러온다. (상세 내용 포함)"
    )
    @GetMapping("/sns/posts")
    public List<SnsFindResponse> findAllSnsPosts() {

        List<SnsFindResponse> snsFindResponseList = new ArrayList<>();
        List<SnsPost> snsPostsList = snsService.findAllSnsPosts();

        for (SnsPost snsPost : snsPostsList) {

            SnsFindResponse snsFindResponse = createSnsFindResponse(snsPost);
            snsFindResponseList.add(snsFindResponse);
        }

        return snsFindResponseList;
    }

    @ApiOperation(
            value = "Sns 게시글 내용 수정",
            notes = "유저가 수정한 Sns 게시글 내용을 저장한다."
    )
    @PutMapping(path = "/sns/posts/{snsPostId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String update(@ApiParam(value = "게시글 파일 (여러개 개능)") @RequestPart(required = false) List<MultipartFile> snsPostsImageOrVideoFileList,
                         @ApiParam(value = "Sns 작성 글 id 값") @PathVariable Long snsPostId,
                         @ApiParam(value = "Sns 게시글 내용") @RequestPart String snsPostContent) throws SnsPostNotFound {
        snsService.update(snsPostId, snsPostContent, snsPostsImageOrVideoFileList);

        return "수정 완료";
    }

    @ApiOperation(
            value = "Sns 게시글 삭제",
            notes = "유저가 선택한 Sns 게시글을 삭제한다."
    )
    @DeleteMapping("/sns/posts/{snsPostId}")
    public String deletePost(@ApiParam(value = "Sns 게시 글 id 값") @PathVariable Long snsPostId) {
        snsService.deletePost(snsPostId);

        return "삭제 완료";
    }


    @ApiOperation(
            value = "Sns 유저 팔로우",
            notes = "한 유저(follwingMember)가 다른 유저(followedMember)를 팔로우 한다."
    )
    @PostMapping("/sns/follow/{followingMemberId}/{followedMemberId}")
    public Long follow(@ApiParam(value = "팔로우 하는 사람 id") @PathVariable Long followingMemberId,
                       @ApiParam(value = "팔로우 받는 사람 id") @PathVariable Long followedMemberId) throws MemberNotFound, SnsFollowingNotFound {
        return snsService.follow(followedMemberId, followingMemberId);
    }

    @ApiOperation(
            value = "Sns 유저 언팔로우",
            notes = "한 유저(follwingMember)가 다른 유저(followedMember)를 언팔로우 한다."
    )
    @DeleteMapping("/sns/follow/{followingMemberId}/{followedMemberId}")
    public String unFollow(@ApiParam(value = "팔로우 취소 하는 사람 id") @PathVariable Long followingMemberId,
                           @ApiParam(value = "팔로우 취소 받는 사람 id") @PathVariable Long followedMemberId) throws MemberNotFound, SnsFollowingNotFound {

        snsService.unFollow(followedMemberId, followingMemberId);

        return "팔로우 취소 완료";
    }

    @ApiOperation(value = "Sns 작성 글 좋아요 누르기", notes = "한 번 누르면 좋아요 1 증가, 그 다음 누르면 1 감소, 그 다음 누르면 1 증가, ... (토글 식)")
    @PutMapping("/sns/posts/{snsPostId}/likes/members/{memberId}")
    public int postLike(@ApiParam(value = "좋아요 누른 Sns 작성 글 id 값") @PathVariable Long snsPostId,
                        @ApiParam(value = "좋아요 누른 Member Id 값") @PathVariable Long memberId)
            throws SnsPostNotFound, MemberNotFound {
        return snsService.postLike(snsPostId, memberId);
    }


    @ApiOperation(
            value = "댓글 달기",
            notes = "유저가 작성한 댓글을 저장한다."
    )
    @PostMapping("/sns/posts/{snsPostId}/comments/members/{memberId}")
    public Long addComment(@ApiParam(value = "댓글 내용", example = "댓글 내용") @RequestBody SnsCommentRequset snsCommentRequest,
                           @ApiParam(value = "댓글 단 작성 글 id") @PathVariable Long snsPostId,
                           @ApiParam(value = "멤버 id") @PathVariable Long memberId) throws SnsPostNotFound, MemberNotFound {

        SnsPost snsPost = snsService.findOne(snsPostId);
        Member member = memberService.findMemberById(memberId);

        SnsComment snsComment = SnsComment.builder()
                .snsPost(snsPost).snsComment(snsCommentRequest.getSnsComment()).member(member).build();

        snsPost.addComment(snsComment);

        return snsService.saveComment(snsComment);
    }

    @ApiOperation(
            value = "댓글 조회",
            notes = "특정 글에 달린 모든 댓글들을 조회한다."
    )
    @GetMapping("/sns/posts/{snsPostId}/comment")
    public List<SnsCommentResponse> findAllComment(@ApiParam(value = "sns 작성글 id", example = "1") @PathVariable Long snsPostId) throws SnsPostNotFound {

        List<SnsCommentResponse> snsCommentResponsesList = new ArrayList<>();
        List<SnsComment> snsCommentsList = snsService.findAllComment(snsPostId);

        for (SnsComment snsComment : snsCommentsList) {
            SnsCommentResponse snsCommentResponse = createSnsCommentResponse(snsComment);
            snsCommentResponsesList.add(snsCommentResponse);
        }
        return snsCommentResponsesList;
    }

    @ApiOperation(
            value = "Sns 작성 글 신고",
            notes = "Sns 작성 글을 신고하고 해당 기록을 db에 저장한다.\n"
    )
    @PostMapping("/sns/report/posts/{snsPostId}/members/{memberId}")
    public Long reportSnsPost(@ApiParam(value = "신고 당한 sns글 id", example = "1") @PathVariable Long snsPostId,
                              @ApiParam(value = "신고자 id", example = "1") @PathVariable Long memberId,
                              @ApiParam(value = "신고 사유", example = "신고 사유") @RequestParam String reportReason) throws MemberNotFound, SnsPostNotFound, DuplicateReporter, WriterEqualsReporter {
        return snsService.reportSnsPost(snsPostId, memberId, reportReason);
    }

    @ApiOperation(
            value = "Sns 댓글 신고",
            notes = "Sns 댓글을 신고하고 해당 기록을 db에 저장한다.\n"
    )
    @PostMapping("/sns/report/comments/{snsCommentId}/members/{memberId}")
    public Long reportSnsComment(@ApiParam(value = "신고 당한 sns 댓글 id", example = "1") @PathVariable Long snsCommentId,
                                 @ApiParam(value = "신고자 id", example = "1") @PathVariable Long memberId,
                                 @ApiParam(value = "신고 사유", example = "신고 사유") @RequestParam String reportReason) throws MemberNotFound, SnsPostNotFound, SnsCommentNotFound, DuplicateReporter, WriterEqualsReporter {
        return snsService.reportSnsComment(snsCommentId, memberId, reportReason);
    }

    @ApiOperation(
            value = "같은 헬스장 다니는 유저 조회",
            notes = "등록된 헬스장이 같은 유저들을 조회한다.\n"
                    + "헬스장 이름이 아니라 카카오 로컬 api에서의 id가 같은 조건으로 검색\n"
    )
    @GetMapping("/sns/members/{memberId}/same-gym")
    public List<MemberProfileFindResponse> findAllMemberByGymId(@ApiParam(value = "멤버 id 값", example = "1") @PathVariable Long memberId) throws MemberNotFound {

        List<MemberProfileFindResponse> memberProfileFindResponseList = new ArrayList<>();
        List<Member> memberList = memberService.findMemberByGymId(memberId);

        for (Member member : memberList) {
            log.info("gym-id : {}", member.getGymId());
        }

        addMemberProfileFindResponseToList(memberProfileFindResponseList, memberList);

        return memberProfileFindResponseList;
    }

    @ApiOperation(
            value = "팔로우 목록 조회",
            notes = "멤버가 팔로우 한 다른 멤버들의 id를 리스트 형태로 반환한다.\n"
    )
    @GetMapping("/sns/members/{memberId}/follow-members")
    public List<Long> findAllFollowMembers(@ApiParam(value = "멤버 id 값", example = "1") @PathVariable Long memberId) throws MemberNotFound {
        return snsService.findFollowingMembers(memberId);
    }

    private void addMemberProfileFindResponseToList(List<MemberProfileFindResponse> memberProfileFindResponseList, List<Member> memberList) {
        for (Member member : memberList) {

            MemberProfileFindResponse memberProfileFindResponse = MemberProfileFindResponse.builder()
                    .memberHeight(member.getHeight())
                    .memberName(member.getName())
                    .memberRegisteredGymName(member.getGymName())
                    .memberWeight(member.getWeight())
                    .customProfileImageUrl(member.getCustomProfileImageUrl())
                    .memberId(member.getId())
                    .build();

            memberProfileFindResponseList.add(memberProfileFindResponse);
        }
    }

    private SnsFindResponse createSnsFindResponse(SnsPost snsPost) {
        List<String> snsPostImageOrVideoLinkList = new ArrayList<>();

        for (SnsPostImageOrVideo snsPostImageOrVideo : snsPost.getSnsPostImageOrVideoList()) {
            snsPostImageOrVideoLinkList.add(snsPostImageOrVideo.getImageOrVideoUrl());
        }

        SnsFindResponse snsFindResponse = SnsFindResponse.builder()
                .snsPostId(snsPost.getId())
                .memberId(snsPost.getMember().getId())
                .snsContent(snsPost.getContent())
                .snsImageOrVideoLinkList(snsPostImageOrVideoLinkList)
                .snsLikesNum(snsPost.getLikes())
                .memberName(snsPost.getMember().getName())
                .memberProfilePhotoUrl(snsPost.getMember().getCustomProfileImageUrl())
                .build();

        return snsFindResponse;
    }

    private SnsCommentResponse createSnsCommentResponse(SnsComment snsComment) {
        SnsCommentResponse snsCommentResponse = SnsCommentResponse.builder()
                .snsComment(snsComment.getSnsComment())
                .build();

        return snsCommentResponse;
    }
}

