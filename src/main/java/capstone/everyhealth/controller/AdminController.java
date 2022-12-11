package capstone.everyhealth.controller;

import capstone.everyhealth.Book;
import capstone.everyhealth.BooksCreationDto;
import capstone.everyhealth.controller.dto.Challenge.ChallengeCompletedResponse;
import capstone.everyhealth.controller.dto.Challenge.ChallengePostOrUpdateRequest;
import capstone.everyhealth.controller.dto.Stakeholder.*;
import capstone.everyhealth.domain.challenge.Challenge;
import capstone.everyhealth.domain.report.*;
import capstone.everyhealth.domain.stakeholder.Admin;
import capstone.everyhealth.exception.challenge.ChallengeNotFound;
import capstone.everyhealth.exception.challenge.ChallengeParticipantNotFound;
import capstone.everyhealth.exception.report.ChallengeAuthPostReportNotFound;
import capstone.everyhealth.exception.report.SnsCommentReportNotFound;
import capstone.everyhealth.exception.report.SnsPostReportNotFound;
import capstone.everyhealth.exception.stakeholder.AdminLoginFailed;
import capstone.everyhealth.exception.stakeholder.AdminNotFound;
import capstone.everyhealth.service.AdminService;
import capstone.everyhealth.service.ChallengeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Api(tags = {"관리자 페이지 API"})
@Slf4j
public class AdminController {

    private final AdminService adminService;
    private final ChallengeService challengeService;

    @ResponseBody
    @PostMapping("/admins")
    public Long registerAdmin(@RequestBody AdminRegisterRequest adminRegisterRequest) {

        Admin admin = Admin.builder()
                .adminId(adminRegisterRequest.getAdminId())
                .adminPassword(adminRegisterRequest.getAdminPassword())
                .adminName(adminRegisterRequest.getAdminName())
                .adminPhoneNumber(adminRegisterRequest.getAdminPhoneNumber())
                .build();

        return adminService.save(admin);
    }

    @ResponseBody
    @GetMapping("/admins")
    public List<AdminFindResponse> findAdmins() {
        return adminService.findAllAdmins()
                .stream()
                .map(AdminFindResponse::new)
                .collect(Collectors.toList());
    }

    @PutMapping("/admins/{adminId}")
    public void editAdmin(@PathVariable Long adminId,
                          @RequestBody AdminEditRequest adminEditRequest) throws AdminNotFound {
        adminService.updateAdmin(adminId, adminEditRequest);
    }

    @DeleteMapping("/admins/{adminId}")
    public void editAdmin(@PathVariable Long adminId) {
        adminService.deleteAdmin(adminId);
    }

    // 로그인 페이지 렌더링
    @GetMapping("/admins/login")
    public String loginPage(Model model) {
        model.addAttribute("adminLoginRequest", new AdminLoginRequest());
        return "admin_login";
    }

    // 로그인 정보 검증
    @PostMapping("/admins/login")
    public String adminLoginValidation(@Valid AdminLoginRequest adminLoginRequest,
                                       BindingResult result) {

        if (result.hasErrors()) {
            return "admin_login";
        }

        try {
            adminService.adminLoginValidation(adminLoginRequest);
        } catch (AdminLoginFailed e) {
            result.addError(new ObjectError("adminLoginRequest", "일치하는 계정이 없습니다."));
            return "admin_login";
        }

        //return "redirect:/main_page";
        return "main_page";
    }

    @GetMapping("/admins/main")
    public String adminMainPage() {
        return "main_page";
    }

    // sns 작성글 신고글 조회 페이지
    @GetMapping("/admins/report/sns/posts")
    public String findSnsPostReports(Model model) {

        log.info("SNS 작성골 신고글 페이지");

        List<SnsPostReport> snsPostReportList = adminService.findAllSnsPostReports();
        model.addAttribute("snsPostReportList", snsPostReportList);

        return "report/sns_post";
    }

    // Sns 작성글 신고글에 대한 관리자의 제재
    @ApiOperation(
            value = "Sns 작성글 신고글에 대한 관리자의 제재"
    )
    @ResponseBody
    @PostMapping("/admins/report/sns/posts/{snsPostReportId}")
    public String snsCommentReportsPunishment(@ApiParam(value = "sns 작성글 신고글 id") @PathVariable Long snsPostReportId,
                                              @ModelAttribute SnsPostReportPunishRequest snsPostReportPunishRequest) throws SnsPostReportNotFound {
        SnsPostReport snsPostReport = adminService.findSnsPostReport(snsPostReportId);
        SnsPostReportPunishment snsPostReportPunishment = SnsPostReportPunishment.builder()
                .punishReason(snsPostReportPunishRequest.getReason())
                .blockDays(snsPostReportPunishRequest.getBlockDays())
                .snsPostReport(snsPostReport)
                .build();

        adminService.savePunishSnsPostReport(snsPostReportPunishment);

        return "제재 완료";
    }

    // sns 댓글 신고글 조회 페이지
    @GetMapping("/admins/report/sns/comments")
    public String findSnsCommentReports(Model model) {

        log.info("SNS 댓글 신고글 페이지");

        List<SnsCommentReport> snsCommentReportList = adminService.findAllSnsCommentReports();
        model.addAttribute("snsCommentReportList", snsCommentReportList);

        return "report/sns_comment";
    }

    // Sns 댓글 신고글에 대한 관리자의 제재
    @ApiOperation(
            value = "Sns 댓글 신고글에 대한 관리자의 제재"
    )


    @PostMapping("/admins/report/sns/comments/{snsCommentReportId}")
    public String snsCommentReportsPunishment(@ApiParam(value = "sns 댓글 신고글 id") @PathVariable Long snsCommentReportId,
                                              @ModelAttribute SnsCommentReportPunishRequest snsCommentReportPunishRequest) throws SnsCommentReportNotFound {
        SnsCommentReport snsCommentReport = adminService.findSnsCommentReport(snsCommentReportId);
        SnsCommentReportPunishment snsCommentReportPunishment = SnsCommentReportPunishment.builder()
                .punishReason(snsCommentReportPunishRequest.getReason())
                .blockDays(snsCommentReportPunishRequest.getBlockDays())
                .snsCommentReport(snsCommentReport)
                .build();

        adminService.savePunishSnsCommentReport(snsCommentReportPunishment);

        return "제재 완료";
    }

    // 챌린지 인증글 신고글 조회 페이지
    @GetMapping("/admins/report/challenges/auth")
    public String findChallengeAuthPostReports(Model model) {

        log.info("챌린지 인증 신고글 페이지");
        List<ChallengeAuthPostReport> challengeAuthPostReportList = adminService.findAllChallengeAuthPostReports();
        model.addAttribute("challengeAuthPostReportList", challengeAuthPostReportList);

        return "report/challenge_auth";
    }

    // 챌린지 인증글 신고글에 대한 관리자의 제재
    @GetMapping("/admins/report/challenges/auth/{challengeAuthPostReportId}")
    public String punishReport(Model model, @PathVariable String challengeAuthPostReportId) throws ChallengeAuthPostReportNotFound {
        log.info("챌린지 수정 페이지");
        ChallengeAuthPostReport challengeAuthPostReport = adminService.findChallengeAuthPostReport(Long.valueOf(challengeAuthPostReportId));
        model.addAttribute("ChallengeAuthPostReportPunishRequest", new ChallengeAuthPostReportPunishRequest());
        model.addAttribute("challengeAuthPostReport", challengeAuthPostReport);
        return "report/punish_report";
    }

    @PostMapping("/admins/report/challenges/auth/{challengeAuthPostReportId}")
    public String challengeAuthPostReportsPunishment(@PathVariable Long challengeAuthPostReportId,
                                                     @ModelAttribute ChallengeAuthPostReportPunishRequest challengeAuthPostReportPunishRequest) throws ChallengeAuthPostReportNotFound, ChallengeParticipantNotFound {
        ChallengeAuthPostReport challengeAuthPostReport = adminService.findChallengeAuthPostReport(challengeAuthPostReportId);
        ChallengeAuthPostReportPunishment challengeAuthPostReportPunishment = ChallengeAuthPostReportPunishment.builder()
                .punishReason(challengeAuthPostReportPunishRequest.getReason())
                .blockDays(challengeAuthPostReportPunishRequest.getBlockDays())
                .challengeAuthPostReport(challengeAuthPostReport)
                .build();

        adminService.updateIsProcessedOnChallengeAuthPostReport(challengeAuthPostReportPunishRequest.getBlockDays(), challengeAuthPostReportId);
        adminService.savePunishChallengeAuthPostReport(challengeAuthPostReportPunishment);

        return "redirect:http://localhost:8080/admins/report/challenges/auth";
    }

    // 챌린지 조회 페이지
    @GetMapping("/admins/challenges")
    public String findAllChallenges(Model model) {

        List<Challenge> challengeList = challengeService.findAllChallenges();

        model.addAttribute("challengeList", challengeList);

        return "challenge/challenge_list";
    }

    // 챌린지 생성 페이지
    @GetMapping("/admins/challenges/new")
    public String createChallenge(Model model) {
        model.addAttribute("challengePostOrUpdateRequest", new ChallengePostOrUpdateRequest());

        return "challenge/create_challenge";
    }

    @PostMapping("/admins/challenges/new")
    public String aaaa(Model model,
                       ChallengePostOrUpdateRequest challengePostOrUpdateRequest) {

        LocalDateTime startDate = LocalDate.parse(challengePostOrUpdateRequest.getChallengeStartDate(), DateTimeFormatter.ISO_DATE).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(challengePostOrUpdateRequest.getChallengeEndDate(), DateTimeFormatter.ISO_DATE).atStartOfDay();
        int betweenDays = (int) Duration.between(startDate, endDate).toDays();
        int week = (betweenDays + 1) / 7;
        log.info("betweenDays = {}", betweenDays);
        log.info("week = {}", week);

        List<Integer> weekList = new ArrayList<>();
        List<Integer> numPerWeekList = new ArrayList<>();

        for (int i = 0; i < week; i++) {
            weekList.add(i + 1);
        }
        for (int i = 0; i < challengePostOrUpdateRequest.getChallengeNumPerWeek(); i++) {
            numPerWeekList.add(i + 1);
        }
        model.addAttribute("challengePostOrUpdateRequest", challengePostOrUpdateRequest);
        model.addAttribute("weekList", weekList);
        model.addAttribute("numPerWeekList", numPerWeekList);
        model.addAttribute("weekCount", weekList.size());
        model.addAttribute("numPerWeekCount", numPerWeekList.size());
        return "challenge/create_challenge_routine_test";
    }

    // 챌린지 수정 페이지
    @GetMapping("/admins/challenges/{challengeId}/update")
    public String updateChallenge(Model model, @PathVariable String challengeId) throws ChallengeNotFound {

        log.info("챌린지 수정 페이지");
        Challenge challenge = challengeService.find(Long.valueOf(challengeId));
        model.addAttribute("challenge", challenge);

        return "challenge/edit_challenge";
    }

    @DeleteMapping("/admins/challenges/{challengeId}")
    public String deleteChallenge(@PathVariable Long challengeId) {
        //challengeService.delete(challengeId);
        log.info("챌린지 삭제");
        return "redirect:/admins/challenges";
    }

    // 완료된 챌린지 페이지
    @GetMapping("/admins/completed-challenges")
    public String findByCompletedChallengesAndChallengeStatus(Model model) throws ChallengeNotFound {

        List<Challenge> challengeList = challengeService.findAllClosedChallenges();
        List<ChallengeCompletedResponse> challengeCompletedResponseList = new ArrayList<>();
        for (Challenge challenge : challengeList) {

            log.info("ch-fin : {}", challenge.isFinished());
            List<ChallengeAuthPostReport> challengeAuthPostReportList = adminService.findAllChallengeAuthPostReportsByChallenge(challenge);
            boolean isCompleted = challengeAuthPostReportList.isEmpty() ? true : false;
            int totalParticipantNum = challenge.getParticipationNum();
            int succeededParticipantNum = challengeService.findChallengeSucceededParticipantNum(challenge.getId());
            int reward = challengeService.calculateIndividualReward(totalParticipantNum, succeededParticipantNum, challenge.getParticipationFee());

            ChallengeCompletedResponse challengeCompletedResponse = ChallengeCompletedResponse.builder()
                    .challengeId(challenge.getId())
                    .challengeName(challenge.getName())
                    .isCompleted(isCompleted)
                    .finishedDate(challenge.getEndDate())
                    .individulReward(reward)
                    .participateFee(challenge.getParticipationFee())
                    .totalFailed(totalParticipantNum - succeededParticipantNum)
                    .totalParticipated(totalParticipantNum)
                    .totalSuccessed(succeededParticipantNum)
                    .build();

            challengeCompletedResponseList.add(challengeCompletedResponse);
        }

        model.addAttribute("challengeCompletedResponseList", challengeCompletedResponseList);

         return "challenge/completed_challenge_list";
    }

    // 챌린지 정산 : 정산된 멤버 id 반환
    @PostMapping("/admins/challenges/{challengeId}/calculate")
    public String calculateChallenge(@PathVariable Long challengeId) throws ChallengeNotFound {
        adminService.calculateChallenge(challengeId);
        return "redirect:/admins/main";
    }


    /////////////////// 밑에 ApiIgnore는 테스트용으로 무시하셔도 됩니다 /////////////////////

    @ApiIgnore
    @GetMapping("/test")
    public String test1234(Model model) {
        BooksCreationDto booksForm = new BooksCreationDto();

//        for (int i = 1; i <= 3; i++) {
//            booksForm.addBook(new Book());
//        }

        model.addAttribute("form", booksForm);

        return "test";
    }

    @ApiIgnore
    @PostMapping("/test")
    public String test12345(Model model, @ModelAttribute BooksCreationDto form) {
        for (Book book : form.getBooks()) {
            log.info("book = {}", book.getTitle());
            log.info("book = {}", book.getAuthor());
            log.info(" ");
        }
        return "home";
    }

    @ApiIgnore
    @GetMapping("/test22")
    public String test12341234() {
        return "test2";
    }

    @ApiIgnore
    @GetMapping("/test33")
    public String test123412345() {
        return "test3";
    }

    @ApiIgnore
    @GetMapping("/testa")
    public String testaaaa() {
        return "challenge/jquery_test";
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
