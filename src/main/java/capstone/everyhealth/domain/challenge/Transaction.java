package capstone.everyhealth.domain.challenge;

import lombok.Getter;
import capstone.everyhealth.domain.stakeholder.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Transaction {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime date;
    private String content;
}
