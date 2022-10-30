package capstone.everyhealth.domain.stakeholder;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Admin {

    @Id @GeneratedValue
    private Long id;

    private String adminId;
    private String password;
}
