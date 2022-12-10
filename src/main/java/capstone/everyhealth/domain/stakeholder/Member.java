package capstone.everyhealth.domain.stakeholder;

import capstone.everyhealth.domain.challenge.ChallengeAuthPost;
import capstone.everyhealth.domain.challenge.ChallengeParticipant;
import capstone.everyhealth.domain.report.SnsCommentReport;
import capstone.everyhealth.domain.report.SnsPostReport;
import capstone.everyhealth.domain.routine.MemberRoutine;
import capstone.everyhealth.domain.sns.SnsComment;
import capstone.everyhealth.domain.sns.SnsFollowing;
import capstone.everyhealth.domain.sns.SnsPost;
import capstone.everyhealth.domain.sns.SnsPostLikes;
import lombok.*;
import capstone.everyhealth.domain.enums.LoginType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private String loginAt;
    private String socialAccountId;
    private int height;
    private int weight;
    private String gymName;
    private String gymId;
    private String customProfileImageUrl;

    @Builder.Default
    private int point = 0;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeAuthPost> challengeAuthPostList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeParticipant> challengeParticipantList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SnsPost> snsPostList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SnsComment> snsCommentList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SnsPostLikes> snsPostLikes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "followedMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SnsFollowing> snsFollowedMemberList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "followingMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SnsFollowing> snsFollowingMemberList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberRoutine> memberRoutineList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SnsPostReport> snsPostReportList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SnsCommentReport> snsCommentReportList = new ArrayList<>();
}
