package info.alaz.sensor.co2.config;

import com.fasterxml.classmate.TypeResolver;
import info.alaz.sensor.co2.constant.APITags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

import static info.alaz.sensor.co2.web.rest.ParentCo2SensorController.CO2_SENSOR_API_V1_RESPONSE_MEDIA_TYPE;
import static info.alaz.sensor.co2.web.rest.ParentCo2SensorController.SWAGGER_VERSIONING;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Value("${domain-name}")
    private String domainName;

    private final TypeResolver typeResolver;

    @Autowired
    public SwaggerConfiguration(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName(SWAGGER_VERSIONING)
                .tags(new Tag(APITags.CO2_SENSOR_API_TAG, APITags.CO2_SENSOR_API_DESCRIPTION, APITags.CO2_SENSOR_API_ORDER))
                .host(domainName)
                .select()
                .apis(p -> { //Only selects calls which have V1 as accept headers
                    if (p != null && p.produces() != null) {
                        for (MediaType mt : p.produces()) {
                            if (mt.toString().equals(CO2_SENSOR_API_V1_RESPONSE_MEDIA_TYPE)) {
                                return true;
                            }
                        }
                    }
                    return false;
                })
                .build()
                .produces(Collections.singleton(CO2_SENSOR_API_V1_RESPONSE_MEDIA_TYPE))
                .forCodeGeneration(true)
                .globalResponseMessage(RequestMethod.GET, responseMessages())
                .globalResponseMessage(RequestMethod.POST, responseMessages())
                .globalResponseMessage(RequestMethod.PUT, responseMessages())
                .globalResponseMessage(RequestMethod.PATCH, responseMessages())
                .globalResponseMessage(RequestMethod.DELETE, responseMessages());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "CO2 SensorEntity API",
                "This is an API to monitor CO2 levels coming from sensors.",
                "1.0",
                null,
                null,
                null,
                null,
                Collections.emptyList()
        );
    }

    private List<ResponseMessage> responseMessages() {
        return Collections.singletonList(new ResponseMessageBuilder()
                .code(401)
                .message("You are not authorized to access this resource. Access denied.")
                .build());
    }

}
