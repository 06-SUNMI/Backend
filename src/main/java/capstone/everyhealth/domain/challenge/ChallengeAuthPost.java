package capstone.everyhealth.domain.challenge;

import lombok.*;
import capstone.everyhealth.domain.stakeholder.Member;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeAuthPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "challenge_routine_id")
    private ChallengeRoutine challengeRoutine;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String photoUrl;
    @Builder.Default
    private int reportedNum = 0;
}
