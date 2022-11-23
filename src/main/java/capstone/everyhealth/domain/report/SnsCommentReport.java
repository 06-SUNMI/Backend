package capstone.everyhealth.domain.report;

import capstone.everyhealth.domain.sns.SnsComment;
import capstone.everyhealth.domain.sns.SnsPost;
import capstone.everyhealth.domain.stakeholder.Member;
import lombok.Data;

import javax.persistence.*;

@Data
public class SnsCommentReport {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reporter_member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "sns_comment_id")
    private SnsComment snsComment;

    private String reason;
}
