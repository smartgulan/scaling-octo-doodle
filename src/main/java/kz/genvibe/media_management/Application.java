package kz.genvibe.media_management;

import kz.genvibe.media_management.client.elevenlabs.ElevenlabsClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;
import org.springframework.web.service.registry.ImportHttpServices;

@SpringBootApplication
@EnableRedisIndexedHttpSession(redisNamespace = "resona:session")
@ImportHttpServices(types = ElevenlabsClient.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
