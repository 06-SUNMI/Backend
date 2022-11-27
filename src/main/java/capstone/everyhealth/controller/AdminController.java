package capstone.everyhealth.controller;

import capstone.everyhealth.controller.dto.Stakeholder.AdminLoginRequest;
import capstone.everyhealth.controller.dto.Stakeholder.ReportedChallengeAuthPostResponse;
import capstone.everyhealth.controller.dto.Stakeholder.ReportedSnsCommentResponse;
import capstone.everyhealth.controller.dto.Stakeholder.ReportedSnsPostResponse;
import capstone.everyhealth.domain.report.ChallengeAuthPostReport;
import capstone.everyhealth.domain.report.SnsCommentReport;
import capstone.everyhealth.domain.report.SnsPostReport;
import capstone.everyhealth.exception.stakeholder.AdminLoginFailed;
import capstone.everyhealth.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"관리자 페이지 API"})
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @ApiOperation(
            value = "관리자 로그인",
            notes = "관리자의 id와 pw를 전달 받아 검증한다."
    )
    @GetMapping("/admins/login")
    public String adminLoginValidation(@RequestBody AdminLoginRequest adminLoginRequest) throws AdminLoginFailed {
        adminService.adminLoginValidation(adminLoginRequest);

        return "관리자 로그인 성공";
    }

    @ApiOperation(
            value = "SNS 신고글 조회",
            notes = "신고당한 SNS 게시글들을 조회한다."
    )
    @GetMapping("/admins/report/sns/posts")
    public List<ReportedSnsPostResponse> findSnsPostReports() {
        List<SnsPostReport> snsPostReportList = adminService.findAllSnsPostReports();
        List<ReportedSnsPostResponse> reportedSnsResponseList = new ArrayList<>();

        addReportedSnsResponseList(snsPostReportList, reportedSnsResponseList);

        return reportedSnsResponseList;
    }

    @ApiOperation(
            value = "SNS 신고 댓글 조회",
            notes = "신고당한 SNS 댓글들을 조회한다."
    )
    @GetMapping("/admins/report/sns/comments")
    public List<ReportedSnsCommentResponse> findSnsCommentReports() {

        List<SnsCommentReport> snsCommentReportList = adminService.findAllSnsCommentReports();
        List<ReportedSnsCommentResponse> reportedSnsCommentResponseList = new ArrayList<>();

        addReportedSnsCommentList(snsCommentReportList, reportedSnsCommentResponseList);

        return reportedSnsCommentResponseList;
    }

    @ApiOperation(
            value = "챌린지 인증 신고글 조회",
            notes = "신고당한 챌린지 인증글들을 조회한다."
    )
    @GetMapping("/admins/report/challenges/auth")
    public List<ReportedChallengeAuthPostResponse> findChallengeAuthPostReports() {

        List<ChallengeAuthPostReport> challengeAuthPostReportList = adminService.findAllChallengeAuthPostReports();
        List<ReportedChallengeAuthPostResponse> reportedChallengeAuthPostResponseList = new ArrayList<>();

        addReportedChallengeAuthPostList(challengeAuthPostReportList, reportedChallengeAuthPostResponseList);

        return reportedChallengeAuthPostResponseList;
    }

    private void addReportedChallengeAuthPostList(List<ChallengeAuthPostReport> challengeAuthPostReportList, List<ReportedChallengeAuthPostResponse> reportedChallengeAuthPostResponseList) {
        for (ChallengeAuthPostReport challengeAuthPostReport : challengeAuthPostReportList) {

            ReportedChallengeAuthPostResponse reportedChallengeAuthPostResponse = ReportedChallengeAuthPostResponse.builder()
                    .reporterMemberId(challengeAuthPostReport.getMember().getId())
                    .challengeAuthPostId(challengeAuthPostReport.getChallengeAuthPost().getId())
                    .reason(challengeAuthPostReport.getReason())
                    .build();

            reportedChallengeAuthPostResponseList.add(reportedChallengeAuthPostResponse);
        }
    }

    private void addReportedSnsCommentList(List<SnsCommentReport> snsCommentReportList, List<ReportedSnsCommentResponse> reportedSnsCommentResponseList) {
        for (SnsCommentReport snsCommentReport : snsCommentReportList) {

            ReportedSnsCommentResponse reportedSnsCommentResponse = ReportedSnsCommentResponse.builder()
                    .reporterMemberId(snsCommentReport.getMember().getId())
                    .snsCommentId(snsCommentReport.getSnsComment().getId())
                    .reason(snsCommentReport.getReason())
                    .build();

            reportedSnsCommentResponseList.add(reportedSnsCommentResponse);
        }
    }


    private void addReportedSnsResponseList(List<SnsPostReport> snsPostReportList, List<ReportedSnsPostResponse> reportedSnsResponseList) {
        for (SnsPostReport snsPostReport : snsPostReportList) {

            ReportedSnsPostResponse reportedSnsResponse = ReportedSnsPostResponse.builder()
                    .reporterMemberId(snsPostReport.getMember().getId())
                    .snsPostId(snsPostReport.getSnsPost().getId())
                    .reason(snsPostReport.getReason())
                    .build();

            reportedSnsResponseList.add(reportedSnsResponse);
        }
    }
}
