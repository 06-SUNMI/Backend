package capstone.everyhealth.repository;

import capstone.everyhealth.domain.stakeholder.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
class StakeholderRepositoryTest {

    /*
    @Autowired
    StakeholderRepository stakeholderRepository;

    @Test
    @DisplayName("멤버 조회 레포지토리 단위 테스트")
    void findMemberTest() {

        // given
        Member member = Member.builder()
                .id(1L)
                .build();

        Member savedMember = stakeholderRepository.save(member);

        log.info("savedMember : {}", savedMember);

        // when
        Optional<Member> foundMember = stakeholderRepository.findById(savedMember.getId());

        log.info("foundMember : {}", foundMember);

        // then
        assertEquals(foundMember.get().getId(), savedMember.getId());
    }

     */
}
