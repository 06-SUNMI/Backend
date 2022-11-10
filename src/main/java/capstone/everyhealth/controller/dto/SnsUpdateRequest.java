package capstone.everyhealth.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SnsUpdateRequest {
    private String snsImageLink;
    private String snsVideoLink;
    private String snsContent;
}
