package capstone.everyhealth.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor

public class SnsService {

    private final MemberRepository snsRepository;

    public List<Member> findByName(String userName) {
       return  snsRepository.findAllByName(userName);
    
    }
}