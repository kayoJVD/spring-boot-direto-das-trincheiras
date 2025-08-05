package academy.devdojo.config;

import external.dependency.Connection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ConnectionConfiguration {
    private final ConnectionConfigurationProperties configurationProperties;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public Connection connection() {
        return new Connection(configurationProperties.url(), configurationProperties.username(), configurationProperties.password());
    }
}
