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
@Setter
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
    private int progressRate;

    public void addMemberRoutineContent(MemberRoutineContent memberRoutineContent) {
        memberRoutineContentList.add(memberRoutineContent);
        memberRoutineContent.changeMemberRoutine(this);
    }

    public void calculateAndSetProgressRate(MemberRoutine memberRoutine) {
        int RoutineContentNum = memberRoutine.getMemberRoutineContentList().size();
        int checkedRoutineContentCount = 0;

        for (MemberRoutineContent memberRoutineContent : memberRoutine.getMemberRoutineContentList()) {
            checkedRoutineContentCount = memberRoutineContent.isMemberRoutineIsChecked() ? checkedRoutineContentCount + 1 : checkedRoutineContentCount;
        }

        this.progressRate = 100 * checkedRoutineContentCount / RoutineContentNum;

    }
}
