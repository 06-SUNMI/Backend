package capstone.everyhealth.service;

import capstone.everyhealth.controller.dto.Stakeholder.MemberEditProfileRequest;
import capstone.everyhealth.controller.dto.Stakeholder.MemberFindResponse;
import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.exception.stakeholder.MemberNotFound;
import capstone.everyhealth.fileupload.service.FileUploadService;
import capstone.everyhealth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final FileUploadService fileUploadService;

    @Transactional
    public Long saveMember(Member member) {
        return memberRepository.save(member).getId();
    }

    public List<Long> findAll() {

        List<Long> memberIdList = new ArrayList<>();

        for (Member member : memberRepository.findAll()) {
            memberIdList.add(member.getId());
        }

        return memberIdList;
    }

    public Member findMemberById(Long memberId) throws MemberNotFound {
        return memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFound(memberId));
    }

    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    @Transactional
    public void updateMemberProfile(Long memberId, MemberEditProfileRequest memberEditProfileRequest, MultipartFile memberProfileImageFile) throws MemberNotFound {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFound(memberId));

        updateMemberData(memberEditProfileRequest, member, memberProfileImageFile);
    }

    public List<Member> findAllMember() {
        return memberRepository.findAll();
    }

    private void updateMemberData(MemberEditProfileRequest memberEditProfileRequest, Member member, MultipartFile memberProfileImageFile) {

        String customProfileImageUrl;

        try {
            customProfileImageUrl = fileUploadService.uploadImage(memberProfileImageFile);
        } catch (NullPointerException e) {
            customProfileImageUrl = null;
        }

        member.setName(memberEditProfileRequest.getMemberName());
        member.setCustomProfileImageUrl(customProfileImageUrl);
        member.setGymName(memberEditProfileRequest.getMemberRegisteredGymName());
        member.setGymId(memberEditProfileRequest.getMemberRegisteredGymId());
        member.setHeight(memberEditProfileRequest.getMemberHeight());
        member.setWeight(memberEditProfileRequest.getMemberWeight());
    }
}
