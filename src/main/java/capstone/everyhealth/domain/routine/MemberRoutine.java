package capstone.everyhealth.domain.routine;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import capstone.everyhealth.domain.stakeholder.Member;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRoutine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "memberRoutine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberRoutineContent> memberRoutineContentList = new ArrayList<>();

    private String routineRegisterdate;

    public void addMemberRoutineContent(MemberRoutineContent memberRoutineContent){
        memberRoutineContentList.add(memberRoutineContent);
        memberRoutineContent.changeMemberRoutine(this);
    }
}
