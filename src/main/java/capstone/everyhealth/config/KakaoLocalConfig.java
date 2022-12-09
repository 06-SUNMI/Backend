package capstone.everyhealth.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class KakaoLocalConfig {

    @Value("${kakao.local.client-id}")
    private String cliendId;

    @Value("${kakao.local.url}")
    private String url;
}
