package capstone.everyhealth.domain.sns;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import capstone.everyhealth.domain.stakeholder.Member;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SnsFollowing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member followedMember;

    @ManyToOne
    @JoinColumn(name = "followed_member_id")
    private Member followingMember;
}
