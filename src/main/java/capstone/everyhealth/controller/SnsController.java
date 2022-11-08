package capstone.everyhealth.controller;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.service.SnsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SnsController {

    private final SnsService snsService;

    @GetMapping("/users/{userName}")
    public List<Member> search(@PathVariable String userName){

        log.info(userName);

        return snsService.findByName(userName);
    }
    
}
