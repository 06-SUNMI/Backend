package capstone.everyhealth.domain.stakeholder;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String adminId;
    private String adminPassword;
    private String adminName;
    @Builder.Default
    private boolean isSuperAdmin = false;
}
