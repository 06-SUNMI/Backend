package capstone.everyhealth.controller;


import java.util.List;

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
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@Slf4j
public class SnsController {

    private final SnsService snsService;
    private final MemberService memberService;


    @GetMapping("/users/{userName}")
    public List<Member> search(@PathVariable String userName){

        log.info(userName);

        return snsService.findByName(userName);
    }

    @PostMapping("/sns/{userId}")

    public Long save(@RequestBody SnsPostRequest snsPostRequest, @PathVariable Long userId) {

        Member member = memberService.findMemberById(userId);

        SnsPost snsPost = SnsPost.builder().member(member).content(snsPostRequest.getSnsContent()).videoLink(snsPostRequest.getSnsVideoLink()).imageLink(snsPostRequest.getSnsImageLink()).build();



        return snsService.save(snsPost);
    }


}
