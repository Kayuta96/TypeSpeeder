package se.ju23.typespeeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import java.util.Scanner;

@SpringBootApplication
public class TypeSpeederApplication {

    public static void main(String[] args) {
        SpringApplication.run(TypeSpeederApplication.class, args);
    }

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }

    @Bean
    public CommandLineRunner run(Menu menu, Scanner scanner) {
        return args -> menu.start();
    }
}