// app/BootApp.java
package app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("app.repo")
@EntityScan("app.model")
public class BootApp {
    public static void main(String[] args) { SpringApplication.run(BootApp.class, args); }
}
