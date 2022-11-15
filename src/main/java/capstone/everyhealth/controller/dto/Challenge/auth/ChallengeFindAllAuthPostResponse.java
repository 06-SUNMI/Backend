package capstone.everyhealth.controller.dto.Challenge.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeFindAllAuthPostResponse {

    private List<ChallengeFindAllAuthPostData> challengeFindAllAuthPostDataList = new ArrayList<>();
}
