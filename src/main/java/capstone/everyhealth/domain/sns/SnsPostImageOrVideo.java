package capstone.everyhealth.domain.sns;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SnsPostImageOrVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sns_post_id")
    private SnsPost snsPost;

    private String imageOrVideoUrl;
}
