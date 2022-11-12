package capstone.everyhealth.controller;

import java.util.ArrayList;
import java.util.List;

import capstone.everyhealth.controller.dto.Sns.SnsFindResponse;
import capstone.everyhealth.controller.dto.Sns.SnsUpdateRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import capstone.everyhealth.controller.dto.Sns.SnsPostRequest;
import capstone.everyhealth.domain.sns.SnsPost;
import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.service.MemberService;
import capstone.everyhealth.service.SnsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SnsController {

    private final SnsService snsService;
    private final MemberService memberService;

    @GetMapping("/users/{userName}")
    public List<Member> search(@PathVariable String userName) {

        log.info(userName);

        return snsService.findByName(userName);
    }

    @PostMapping("/sns/{userId}")

    public Long save(@RequestBody SnsPostRequest snsPostRequest, @PathVariable Long userId) {

        Member member = memberService.findMemberById(userId);

        SnsPost snsPost = SnsPost.builder().member(member).content(snsPostRequest.getSnsContent()).videoLink(snsPostRequest.getSnsVideoLink()).imageLink(snsPostRequest.getSnsImageLink()).build();

        return snsService.save(snsPost);
    }

    @GetMapping("/sns/{snsId}")

    public SnsFindResponse findOne(@PathVariable Long snsId) {

        SnsPost snsPost = snsService.findOne(snsId);
        SnsFindResponse snsFindResponse = SnsFindResponse.builder()
                .snsContent(snsPost.getContent())
                .snsVideoLink(snsPost.getVideoLink())
                .snsImageLink(snsPost.getImageLink())
                .memberId(snsPost.getMember().getId())
                .build();
        return snsFindResponse;
    }

    @GetMapping("/sns")
    public List<SnsFindResponse> findAllMembers() {

        List<SnsFindResponse> snsFindResponseList = new ArrayList<>();
        List<SnsPost> snsPostsList = snsService.findAll();

        log.info("before");

        for (SnsPost snsPost : snsPostsList) {

            SnsFindResponse snsFindResponse = createSnsFindResponse(snsPost);
            snsFindResponseList.add(snsFindResponse);
        }

        log.info("after");

        return snsFindResponseList;
    }

    @PutMapping("/sns/{snsId}")
    public void update(@RequestBody SnsUpdateRequest snsUpdateRequest, @PathVariable Long snsId) {

         snsService.update(snsUpdateRequest,snsId);
    }

    private SnsFindResponse createSnsFindResponse(SnsPost snsPost) {
        SnsFindResponse snsFindResponse = SnsFindResponse.builder()
        .memberId(snsPost.getId())
        .snsContent(snsPost.getContent())
        .snsImageLink(snsPost.getImageLink())
        .snsVideoLink(snsPost.getVideoLink())
        .build();

        return snsFindResponse;
    }

}
