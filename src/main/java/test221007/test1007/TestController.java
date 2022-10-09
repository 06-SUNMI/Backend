package test221007.test1007;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class TestController {

    @GetMapping("/home")
    public String home(){
        return "home";
    }

    @ResponseBody
    @GetMapping("/test")
    public String test(){
        return TestDomain.CONTENT;
    }
}
