package academy.devdojo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationProperties(prefix = "datasource")
@ConfigurationPropertiesScan
public record ConnectionConfigurationProperties(String url, String username, String password) {
}
