package dmu.dasom.dasom_homepage.controller.signup;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class WebConfig {

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
                .allowedMethods("GET", "POST");
    }
}
