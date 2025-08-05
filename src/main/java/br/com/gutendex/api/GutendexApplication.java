package br.com.gutendex.api;

import br.com.gutendex.api.principal.Principal;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GutendexApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(GutendexApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Principal principal = new Principal();
        try {
            principal.exibeMenu();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
