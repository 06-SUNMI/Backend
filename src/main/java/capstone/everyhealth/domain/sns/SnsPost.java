package capstone.everyhealth.domain.sns;

import lombok.Getter;
import capstone.everyhealth.domain.stakeholder.User;

import javax.persistence.*;

@Entity
@Getter
public class SnsPost {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String imageLink;
    private String videoLink;
    private String content;
}
