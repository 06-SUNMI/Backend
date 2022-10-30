package capstone.everyhealth.controller.stakeholder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class UserController {

    @ResponseBody
    @PostMapping("/oauth-redirect-kakao")
    public String kakaoCode(@RequestParam String code){
        log.info(code);
        return code;
    }
}
