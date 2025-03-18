package br.com.ecommerce;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication
@PropertySources({
		@PropertySource("classpath:application.yml"),
		@PropertySource("classpath:events.properties")
})
public class EcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("UTC")));
	}

}
