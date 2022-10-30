package capstone.everyhealth.repository.stakeholder;

import lombok.Getter;
import capstone.everyhealth.controller.enums.LoginType;

import javax.persistence.*;

@Entity
@Getter
public class UserRepository {

    @Id @GeneratedValue
    private Long id;

    @Column(name = "social_account_num_id")
    private Long social_id;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    private int height;
    private int weight;
    private String gymName;
}
