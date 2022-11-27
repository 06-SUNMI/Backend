package capstone.everyhealth.domain.report;

import capstone.everyhealth.domain.challenge.ChallengeAuthPost;
import lombok.*;
import capstone.everyhealth.domain.stakeholder.Member;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeAuthPostReport {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reporter_member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "challenge_auth_post_id")
    private ChallengeAuthPost challengeAuthPost;

    private String reason;
}
