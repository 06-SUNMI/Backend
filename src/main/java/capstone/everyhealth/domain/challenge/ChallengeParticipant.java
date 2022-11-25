package capstone.everyhealth.domain.challenge;

import capstone.everyhealth.domain.enums.ChallengePaymentStatus;
import capstone.everyhealth.domain.enums.ChallengeStatus;
import lombok.*;
import capstone.everyhealth.domain.stakeholder.Member;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    private int completedRoutinesNum = 0;

    @Enumerated(EnumType.STRING)
    private ChallengeStatus challengeStatus;

    @OneToOne(mappedBy = "challengeParticipant")
    private ChallengeTransaction challengeTransaction;
}
