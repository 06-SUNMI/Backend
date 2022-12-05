package capstone.everyhealth.domain.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeAuthPostReportPunishment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "challenge_auth_post_report_id")
    private ChallengeAuthPostReport challengeAuthPostReport;

    private String punishReason;
    private int blockDays;
}
