package assignment.presentation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
	"assignment.application.infrastructure",
})
@EnableJpaRepositories(basePackages = {
	"assignment.application.infrastructure",
})
@ComponentScan(basePackages = {
	"assignment.presentation",

	"assignment.application.service",

	"assignment.application.domain",
	"assignment.application.infrastructure",

	"assignment.application.exception",
	"assignment.application.core"
})
public class AssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssignmentApplication.class, args);
	}

}
