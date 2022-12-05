package capstone.everyhealth.domain.report;

import capstone.everyhealth.domain.sns.SnsComment;
import capstone.everyhealth.domain.sns.SnsPost;
import capstone.everyhealth.domain.stakeholder.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @Builder.Default
    private boolean isProcessed = false;
    private LocalDate registeredDate;
}
