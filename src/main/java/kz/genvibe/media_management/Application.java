package kz.genvibe.media_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;

@SpringBootApplication
//@EnableRedisIndexedHttpSession(redisNamespace = "resona:session")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
