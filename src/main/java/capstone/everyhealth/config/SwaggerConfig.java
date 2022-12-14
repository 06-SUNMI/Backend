package capstone.everyhealth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// api 명세
// http://localhost:8080/swagger-ui/#/
// swagger 커스터마이징
// https://github.com/taehyeon3549/SwaggerCustomizing

@EnableSwagger2
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("capstone.everyhealth.controller")) // 특정 패키지경로를 API문서화 한다. 1차 필터
                .paths(PathSelectors.any()) // apis중에서 특정 path조건 API만 문서화 하는 2차 필터
                .build()
                .groupName("API 1.0.0") // group별 명칭을 주어야 한다.
                .pathMapping("/")
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(true); // 400,404,500 .. 표기를 ui에서 삭제한다.
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Every Health API")
                .description("멤버 등록 ㅡ> 루틴 등록 ㅡ> 루틴 조회 / 수정 / 삭제 순으로 진행\n"
                        + "에러 시 파라미터로 준 id를 가지는 멤버나 루틴 등이 존재하지 않을 가능성이 높음\n"
                        + "ex) 멤버 등록 안하고 바로 루틴 등록\n"
                        + "실행 결과는 Try it out ㅡ> Execute 후 나오는 Server response만 보면 됨\n"
                        + "Curl, Responses는 무시\n\n"
                        + "API 실행 결과가 이상하거나 데이터 전달 수령 및 파싱 부분 잘 안되면 바로 알려주세요"
                )
                .version("1.0.0")
                .termsOfServiceUrl("")
//                .contact()
                .license("")
                .licenseUrl("")
                .build()
                ;
    }
}
