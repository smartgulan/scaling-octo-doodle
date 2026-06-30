package kz.genvibe.media_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class Application {

	/**
	 * Absolute timestamps (audit fields, jingle slot play times) are stored as UTC
	 * {@link java.time.Instant}s and wall-clock jingle scheduling resolves through each
	 * store's own zone, so neither depends on this default. It only fixes the fallback
	 * zone for the remaining zoneless {@code LocalDate.now()} calls (e.g. analytics
	 * "today" snapshots) so they stay business-local regardless of the host config.
	 */
	private static final String APP_TIME_ZONE = "Asia/Almaty";

	static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone(APP_TIME_ZONE));
		SpringApplication.run(Application.class, args);
	}

}
