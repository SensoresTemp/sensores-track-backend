package sensores.track.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SensoresApplication {

	public static void main(String[] args) {
		SpringApplication.run(SensoresApplication.class, args);
	}

}
