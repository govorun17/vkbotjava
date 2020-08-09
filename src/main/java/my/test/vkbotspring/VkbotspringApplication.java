package my.test.vkbotspring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class VkbotspringApplication {

	public static void main(String[] args) {
		log.info("!!!app started!!!");
		SpringApplication.run(VkbotspringApplication.class, args);
	}

}
