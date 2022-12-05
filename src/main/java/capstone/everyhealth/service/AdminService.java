package capstone.everyhealth.service;

import capstone.everyhealth.controller.dto.Stakeholder.*;
import capstone.everyhealth.domain.report.*;
import capstone.everyhealth.domain.stakeholder.Admin;
import capstone.everyhealth.exception.report.ChallengeAuthPostReportNotFound;
import capstone.everyhealth.exception.report.SnsCommentReportNotFound;
import capstone.everyhealth.exception.report.SnsPostReportNotFound;
import capstone.everyhealth.exception.stakeholder.AdminLoginFailed;
import capstone.everyhealth.exception.stakeholder.AdminNotFound;
import capstone.everyhealth.repository.*;
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
    private final ChallengeAuthPostReportPunishmentRepository challengeAuthPostReportPunishmentRepository;
    private final SnsCommentReportPunishmentRepository snsCommentReportPunishmentRepository;
    private final SnsPostReportPunishmentRepository snsPostReportPunishmentRepository;

    @Transactional
    public Long save(Admin admin) {
        return adminRepository.save(admin).getId();
    }

    public List<Admin> findAllAdmins() {
        return adminRepository.findAll();
    }

    @Transactional
    public void updateAdmin(Long adminId, AdminEditRequest adminEditRequest) throws AdminNotFound {

        Admin admin = adminRepository.findById(adminId).orElseThrow(()->new AdminNotFound(adminId));

        updateAdminField(adminEditRequest, admin);
    }

    @Transactional
    public void deleteAdmin(Long adminId){
        adminRepository.deleteById(adminId);
    }

    private void updateAdminField(AdminEditRequest adminEditRequest, Admin admin) {
        admin.setAdminPassword(adminEditRequest.getAdminPassword());
        admin.setAdminName(adminEditRequest.getAdminName());
        admin.setAdminPhoneNumber(adminEditRequest.getAdminPhoneNumber());
    }

    public void adminLoginValidation(AdminLoginRequest adminLoginRequest) throws AdminLoginFailed {
        validateAdminLoginRequest(adminLoginRequest);
    }

    public List<SnsPostReport> findAllSnsPostReports() {
        return snsPostReportRepository.findAll();
    }

    public SnsPostReport findSnsPostReport(Long snsPostReportId) throws SnsPostReportNotFound {
        return snsPostReportRepository.findById(snsPostReportId).orElseThrow(()->new SnsPostReportNotFound(snsPostReportId));
    }

    public List<SnsCommentReport> findAllSnsCommentReports() {
        return snsCommentReportRepository.findAll();
    }

    public SnsCommentReport findSnsCommentReport(Long snsCommentReportId) throws SnsCommentReportNotFound {
        return snsCommentReportRepository.findById(snsCommentReportId).orElseThrow(()->new SnsCommentReportNotFound(snsCommentReportId));
    }
    public List<ChallengeAuthPostReport> findAllChallengeAuthPostReports() {
        return challengeAuthPostReportRepository.findAll();
    }

    public ChallengeAuthPostReport findChallengeAuthPostReport(Long challengeAuthPostReportId) throws ChallengeAuthPostReportNotFound {
        return challengeAuthPostReportRepository.findById(challengeAuthPostReportId).orElseThrow(()->new ChallengeAuthPostReportNotFound(challengeAuthPostReportId));
    }

    @Transactional
    public void savePunishSnsPostReport(SnsPostReportPunishment snsPostReportPunishment) {
        snsPostReportPunishmentRepository.save(snsPostReportPunishment);
    }

    @Transactional
    public void savePunishSnsCommentReport(SnsCommentReportPunishment snsCommentReportPunishment) {
        snsCommentReportPunishmentRepository.save(snsCommentReportPunishment);
    }

    @Transactional
    public void savePunishChallengeAuthPostReport(ChallengeAuthPostReportPunishment challengeAuthPostReportPunishment) {
        challengeAuthPostReportPunishmentRepository.save(challengeAuthPostReportPunishment);
    }

    private void validateAdminLoginRequest(AdminLoginRequest adminLoginRequest) throws AdminLoginFailed {
        adminRepository.findByAdminIdAndAdminPassword(adminLoginRequest.getAdminId(), adminLoginRequest.getAdminPassword()).orElseThrow(() -> new AdminLoginFailed());
    }
}
