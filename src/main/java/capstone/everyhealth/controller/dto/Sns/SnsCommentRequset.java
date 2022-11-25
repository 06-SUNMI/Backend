package capstone.everyhealth.controller.dto.Sns;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class SnsCommentRequset {
    private String snsComment;
}
