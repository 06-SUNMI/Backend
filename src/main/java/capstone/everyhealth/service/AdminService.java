package capstone.everyhealth.service;

import capstone.everyhealth.controller.dto.Stakeholder.AdminLoginRequest;
import capstone.everyhealth.domain.report.ChallengeAuthPostReport;
import capstone.everyhealth.domain.report.SnsCommentReport;
import capstone.everyhealth.domain.report.SnsPostReport;
import capstone.everyhealth.exception.stakeholder.AdminLoginFailed;
import capstone.everyhealth.repository.AdminRepository;
import capstone.everyhealth.repository.ChallengeAuthPostReportRepository;
import capstone.everyhealth.repository.SnsCommentReportRepository;
import capstone.everyhealth.repository.SnsPostReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AdminService {

    private final AdminRepository adminRepository;
    private final SnsPostReportRepository snsPostReportRepository;
    private final SnsCommentReportRepository snsCommentReportRepository;
    private final ChallengeAuthPostReportRepository challengeAuthPostReportRepository;

    public void adminLoginValidation(AdminLoginRequest adminLoginRequest) throws AdminLoginFailed {
        validateAdminLoginRequest(adminLoginRequest);
    }

    public List<SnsPostReport> findAllSnsPostReports() {
        return snsPostReportRepository.findAll();
    }

    public List<SnsCommentReport> findAllSnsCommentReports() {
        return snsCommentReportRepository.findAll();
    }

    public List<ChallengeAuthPostReport> findAllChallengeAuthPostReports() {
        return challengeAuthPostReportRepository.findAll();
    }

    private void validateAdminLoginRequest(AdminLoginRequest adminLoginRequest) throws AdminLoginFailed {
        adminRepository.findByAdminIdAndPassword(adminLoginRequest.getAdminId(), adminLoginRequest.getAdminPassword()).orElseThrow(()->new AdminLoginFailed());
    }
}
