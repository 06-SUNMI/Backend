package capstone.everyhealth.controller.dto.Challenge;

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
public class ChallengeFindAllResponse {

    private List<ChallengeFindResponse> challengeFindResponseList = new ArrayList<>();
}
