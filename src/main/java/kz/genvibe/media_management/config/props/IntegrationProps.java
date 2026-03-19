package kz.genvibe.media_management.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "integration")
@Data
@Validated
public class IntegrationProps {

    public record ModerntechProperties(
        String baseUrl,
        String hmacSecret
    ) {}

}
