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
public class SnsUpdateRequest {
    @ApiModelProperty(value = "수정한 글 내용", example = "수정 내용")
    private String snsContent;
}
