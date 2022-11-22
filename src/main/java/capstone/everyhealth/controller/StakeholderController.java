package capstone.everyhealth.controller;

import capstone.everyhealth.controller.dto.Stakeholder.MemberCreateRequest;
import capstone.everyhealth.controller.dto.Stakeholder.MemberFindResponse;
import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.exception.stakeholder.MemberNotFound;
import capstone.everyhealth.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"임시 멤버 데이터 조작을 위한 API"})
public class StakeholderController {

    private final MemberService memberService;

    @ApiOperation(
            value = "임시 멤버 데이터 등록",
            notes = "임시 멤버 데이터를 등록한다"
    )
    @PostMapping("/members")
    public String saveMember(@ApiParam(value="회원 가입 유저 정보") @RequestBody MemberCreateRequest memberCreateRequest) {

        Member member = createMember(memberCreateRequest);

        return "등록 완료, id : " + memberService.saveMember(member);
    }

    @ApiOperation(
            value = "전체 멤버 조회",
            notes = "전체 멤버를 조회한다"
    )
    @GetMapping("/members")
    public List<MemberFindResponse> findAllMembers() {

        List<MemberFindResponse> memberFindResponseList = new ArrayList<>();
        List<Member> memberList = memberService.findAll();

        log.info("before");

        for (Member member : memberList) {

            MemberFindResponse memberFindResponse = createMemberFindResponse(member);
            memberFindResponseList.add(memberFindResponse);
        }

        log.info("after");

        return memberFindResponseList;
    }

    @ApiOperation(
            value = "특정 멤버 조회",
            notes = "특정 멤버를 조회한다."
    )
    @GetMapping("/members/{memberId}")
    public MemberFindResponse findMember(@ApiParam(value = "사용자의 id값", example = "1") @PathVariable Long memberId) throws MemberNotFound {

        Member member = memberService.findMemberById(memberId);
        MemberFindResponse memberFindResponse = createMemberFindResponse(member);

        return memberFindResponse;
    }

    @ApiOperation(
            value = "특정 멤버 삭제",
            notes = "특정 멤버를 삭제 한다."
    )
    @DeleteMapping("/members/{memberId}")
    public String deleteMember(@ApiParam(value = "사용자의 id값", example = "1") @PathVariable Long memberId) {
        memberService.deleteMember(memberId);

        return memberId + "번 멤버 삭제 완료";
    }

    private MemberFindResponse createMemberFindResponse(Member member) {
        return MemberFindResponse.builder()
                .memberId(member.getId())
                .loginAt(member.getLoginAt())
                .memberHeight(member.getHeight())
                .memberName(member.getName())
                .memberWeight(member.getWeight())
                .memberRegisteredGymName(member.getGymName())
                .socialAccountId(member.getSocialAccountId())
                .build();
    }

    private Member createMember(MemberCreateRequest memberCreateRequest) {
        return Member.builder()
                .name(memberCreateRequest.getMemberName())
                .socialAccountId(memberCreateRequest.getSocialAccountId())
                .loginAt(memberCreateRequest.getLoginAt())
                .height(memberCreateRequest.getMemberHeight())
                .weight(memberCreateRequest.getMemberWeight())
                .gymName(memberCreateRequest.getMemberRegisteredGymName())
                .build();
    }
}
