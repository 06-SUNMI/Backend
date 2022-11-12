package capstone.everyhealth.domain.sns;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class SnsComment {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private SnsPost post;

    private String content;
}
