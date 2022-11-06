package capstone.everyhealth.service;

import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.repository.StakeholderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StakeholderService {

    private final StakeholderRepository stakeholderRepository;

    public Optional<Member> findById(Long id) {
        return stakeholderRepository.findById(id);
    }
}
