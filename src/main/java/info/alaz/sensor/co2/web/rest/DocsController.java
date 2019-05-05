package info.alaz.sensor.co2.web.rest;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/api/v1/sensors")
@Api(hidden = true)
public class DocsController {

    private static final String DOCS_URL = "http://localhost:%d/v2/api-docs?group=%s";

    private Logger logger = LoggerFactory.getLogger(DocsController.class);

    @Value("${server.port:8080}")
    private int port;

    @GetMapping(value = "/swagger", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> getSwaggerFile(@RequestParam(name = "group") String group) {
        try {
            String jsonObject = new RestTemplate().getForObject(String.format(DOCS_URL, port, group), String.class);
            return ResponseEntity.ok(jsonObject);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

