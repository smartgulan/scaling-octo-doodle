package kz.genvibe.media_management.client.config;

import kz.genvibe.media_management.config.props.IntegrationProps;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class ElevenlabsClientConfig {

    private final IntegrationProps integrationProps;

    @Bean
    public RestClientHttpServiceGroupConfigurer elevenlabsConfigurer() {
        return groups -> groups
            .filterByName("elevenlabs")
            .forEachClient((group, builder) ->
                builder.baseUrl(integrationProps.getElevenlabs().baseUrl())
            );
    }

}