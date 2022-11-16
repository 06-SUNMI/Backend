package capstone.everyhealth.service;

import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public Member findMemberById(Long member_id) {
        return memberRepository.findById(member_id).get();
    }

    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }
}
