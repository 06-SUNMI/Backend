package capstone.everyhealth.domain.report;

import lombok.*;
import capstone.everyhealth.domain.sns.SnsPost;
import capstone.everyhealth.domain.stakeholder.Member;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SnsPostReport {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reporter_member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "sns_post_id")
    private SnsPost snsPost;

    private String reason;
}
