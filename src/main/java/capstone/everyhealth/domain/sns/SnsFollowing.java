package capstone.everyhealth.domain.sns;

import lombok.Getter;
import capstone.everyhealth.domain.stakeholder.Member;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
@Getter
public class SnsFollowing implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member followedMember;

    @Id
    @ManyToOne
    @JoinColumn(name = "followed_member_id")
    private Member followingMember;
}
