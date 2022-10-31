package capstone.everyhealth.domain.routine;

import lombok.Getter;
import capstone.everyhealth.domain.stakeholder.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class MemberRoutine {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime date;
}
