package capstone.everyhealth.domain.routine;

import lombok.*;
import capstone.everyhealth.domain.stakeholder.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRoutine {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "memberRoutine")
    private List<MemberRoutineContent> routineContentList;

    private String date;
}
