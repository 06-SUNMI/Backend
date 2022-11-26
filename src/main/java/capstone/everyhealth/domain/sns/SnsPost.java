package capstone.everyhealth.domain.sns;

import lombok.*;
import capstone.everyhealth.domain.stakeholder.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SnsPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "snsPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SnsPostImageOrVideo> snsPostImageOrVideoList = new ArrayList<>();

    private String content;

    @Builder.Default
    private int likes = 0;

    @Builder.Default
    @OneToMany(mappedBy = "snsPost",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SnsComment> snsCommentList = new ArrayList<>();

    public void addComment(SnsComment snsComment){
        snsCommentList.add(snsComment);
        snsComment.setSnsPost(this);
    }

    public void addImageOrVideo(SnsPostImageOrVideo snsPostImageOrVideo) {
        snsPostImageOrVideoList.add(snsPostImageOrVideo);
        snsPostImageOrVideo.setSnsPost(this);
    }
}
