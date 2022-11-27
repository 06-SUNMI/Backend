package capstone.everyhealth.domain.sns;

import capstone.everyhealth.domain.stakeholder.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class SnsComment {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sns_post_id")
    private SnsPost snsPost;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String snsComment;
}
