package capstone.everyhealth.domain.sns;

import lombok.Getter;
import capstone.everyhealth.domain.stakeholder.Member;

import javax.persistence.*;

@Entity
@Getter
public class SnsPost {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String imageLink;
    private String videoLink;
    private String content;
}
