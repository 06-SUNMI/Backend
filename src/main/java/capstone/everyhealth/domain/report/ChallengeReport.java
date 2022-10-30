package capstone.everyhealth.domain.report;

import lombok.Getter;
import capstone.everyhealth.domain.sns.SnsPost;
import capstone.everyhealth.domain.stakeholder.User;

import javax.persistence.*;

@Entity
@Getter
public class ChallengeReport {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reporter_user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "sns_post_id")
    private SnsPost snsPost;

    private String reason;
}
