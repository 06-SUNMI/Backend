package capstone.everyhealth.domain.stakeholder;

import lombok.*;
import capstone.everyhealth.domain.enums.LoginType;

import javax.persistence.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "social_account_num_id")
    private Long social_id;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    private int height;
    private int weight;
    private String gymName;
}
