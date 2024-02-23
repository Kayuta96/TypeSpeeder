package se.ju23.typespeeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TypeSpeederApplication {

    public static void main(String[] args) {
        SpringApplication.run(TypeSpeederApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(Menu menu) {
        return args -> menu.start();
    }
}


