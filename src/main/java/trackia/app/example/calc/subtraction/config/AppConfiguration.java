package trackia.app.example.calc.subtraction.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppConfiguration {
    String addition;
    String subtraction;
    String multiplication;
    String division;
}
