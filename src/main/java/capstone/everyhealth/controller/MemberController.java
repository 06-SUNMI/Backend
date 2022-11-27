package capstone.everyhealth.controller;

import capstone.everyhealth.controller.dto.Stakeholder.MemberCreateRequest;
import capstone.everyhealth.controller.dto.Stakeholder.MemberEditProfileRequest;
import capstone.everyhealth.controller.dto.Stakeholder.MemberProfileFindResponse;
import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.exception.stakeholder.MemberNotFound;
import capstone.everyhealth.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"멤버 API"})
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(
            value = "멤버 데이터 등록",
            notes = "소셜 로그인 및 추가 정보 입력 후 받은 멤버 데이터를 전달하여 db에 등록한다.\n"
                    + "헬스장 이름, 헬스장 id는 카카오 로컬 api서 같이 가져올 수 있다."
    )
    @PostMapping("/members")
    public Long saveMember(@ApiParam(value = "회원 가입 유저 정보") @RequestBody MemberCreateRequest memberCreateRequest) {
        Member member = createMember(memberCreateRequest);

        return memberService.saveMember(member);
    }

    @ApiOperation(
            value = "전체 멤버 조회 (테스트용)",
            notes = "현재 db에 저장된 멤버들의 id 리스트를 반환한다."
    )
    @GetMapping("/members")
    public List<Long> findAllMembers() {
        return memberService.findAll();
    }

    @ApiOperation(
            value = "멤버 프로필 조회",
            notes = "특정 멤버의 프로필 정보를 조회한다.\n"
                    + "customProfileImageUrl이 null이면 asset 폴더 내의 기본 프로필 사진을 불러온다."
    )
    @GetMapping("/members/{memberId}")
    public MemberProfileFindResponse findMember(@ApiParam(value = "사용자의 id값", example = "1") @PathVariable Long memberId) throws MemberNotFound {

        Member member = memberService.findMemberById(memberId);
        MemberProfileFindResponse memberProfileFindResponse = createMemberProfileFindResponse(member);

        return memberProfileFindResponse;
    }

    @ApiOperation(
            value = "멤버 프로필 수정",
            notes = "멤버의 프로필 정보를 수정한다.\n"
                    + "헬스장 이름, 헬스장 id는 카카오 로컬 api서 같이 가져올 수 있다."
    )
    @PutMapping(path = "/members/{memberId}")
    public String updateMember(@ApiParam(value = "유저 id 값", example = "1") @PathVariable Long memberId,
                               @ApiParam(value = "수정한 유저 프로필 정보") @ModelAttribute MemberEditProfileRequest memberEditProfileRequest
                               /*@ApiParam(value = "프로필 이미지 파일") @RequestPart MultipartFile memberProfileImageFile*/) throws MemberNotFound {

        log.info("MemberEditProfileRequest = {}",memberEditProfileRequest);
        memberService.updateMemberProfile(memberId, memberEditProfileRequest, memberEditProfileRequest.getMemberProfileImageFile());

        return "수정 완료";
    }

    @ApiOperation(
            value = "특정 멤버 삭제 (테스트용)",
            notes = "특정 멤버를 삭제 한다."
    )
    @DeleteMapping("/members/{memberId}")
    public String deleteMember(@ApiParam(value = "사용자의 id값", example = "1") @PathVariable Long memberId) {
        memberService.deleteMember(memberId);

        return memberId + "번 멤버 삭제 완료";
    }

    private MemberProfileFindResponse createMemberProfileFindResponse(Member member) {
        return MemberProfileFindResponse.builder()
                .memberHeight(member.getHeight())
                .memberName(member.getName())
                .memberWeight(member.getWeight())
                .memberRegisteredGymName(member.getGymName())
                .customProfileImageUrl(member.getCustomProfileImageUrl())
                .build();
    }

    private Member createMember(MemberCreateRequest memberCreateRequest) {
        return Member.builder()
                .name(memberCreateRequest.getMemberName())
                .email(memberCreateRequest.getMemberEmail())
                .socialAccountId(memberCreateRequest.getSocialAccountId())
                .height(memberCreateRequest.getMemberHeight())
                .weight(memberCreateRequest.getMemberWeight())
                .gymName(memberCreateRequest.getMemberRegisteredGymName())
                .gymId(memberCreateRequest.getMemberRegisteredGymId())
                .build();
    }
}
