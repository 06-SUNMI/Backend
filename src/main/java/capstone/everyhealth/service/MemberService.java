package capstone.everyhealth.service;

import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.exception.stakeholder.MemberNotFound;
import capstone.everyhealth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long saveMember(Member member) {
        return memberRepository.save(member).getId();
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findMemberById(Long memberId) throws MemberNotFound {
        return memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFound(memberId));
    }

    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }
}
