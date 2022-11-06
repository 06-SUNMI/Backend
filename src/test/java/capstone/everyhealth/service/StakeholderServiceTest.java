package capstone.everyhealth.service;

import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.repository.StakeholderRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@Slf4j
class StakeholderServiceTest {
/*
    @InjectMocks
    private StakeholderService stakeholderService;

    @Mock
    private StakeholderRepository stakeholderRepository;

    @Test
    @DisplayName("멤버 조회 서비스 단위 테스트")
    void findMemberTest() {

        // given
        Long memberId = 1L;

        when(stakeholderRepository.findById(memberId))
                .thenReturn(
                        Optional.ofNullable(Member.builder().id(memberId).build())
                );

        // when
        Long foundMemberId = stakeholderService.findById(memberId).get().getId();

        // then
        assertEquals(foundMemberId, memberId);
    }

 */
}
