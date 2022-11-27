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
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String socialAccountId;
    private int height;
    private int weight;
    private String gymName;
    private String gymId;
    private String customProfileImageUrl;
}
