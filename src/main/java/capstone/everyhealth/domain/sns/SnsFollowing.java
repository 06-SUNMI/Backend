package capstone.everyhealth.domain.sns;

import lombok.Getter;
import capstone.everyhealth.domain.stakeholder.User;

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
    @JoinColumn(name = "user_id")
    private User followedUser;

    @Id
    @ManyToOne
    @JoinColumn(name = "followed_user_id")
    private User followingUser;
}
