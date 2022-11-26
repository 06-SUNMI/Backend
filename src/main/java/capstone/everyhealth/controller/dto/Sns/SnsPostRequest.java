package capstone.everyhealth.controller.dto.Sns;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SnsPostRequest {

    @ApiModelProperty(value = "sns 글 내용", example = "sns 작성 글 내용")
    private String snsContent;
}
