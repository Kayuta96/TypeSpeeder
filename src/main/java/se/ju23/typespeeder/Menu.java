package se.ju23.typespeeder;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Menu implements MenuService {
    private RankingManager rankingManager;
    private UserRepository userRepository;
    private EntityManager entityManager;
    private User loggedInUser;
    private Language language;
    private Scanner scanner;

    @Autowired
    public Menu(UserRepository userRepository, EntityManager entityManager, RankingManager rankingManager, Scanner scanner) {
        this.userRepository = userRepository;
        this.entityManager = entityManager;
        this.rankingManager = rankingManager;
        this.scanner = scanner;
    }

    public Menu() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        chooseLanguage();
        while (true) {
            System.out.println("------------");
            System.out.println((language == Language.SWEDISH ? "Välkommen till menyn!" : "Welcome to the menu!"));
            System.out.println((language == Language.SWEDISH ? "Vad vill du göra?" : "What would you like to do?"));
            System.out.println("1. " + (language == Language.SWEDISH ? "Logga in" : "Login"));
            System.out.println("2. " + (language == Language.SWEDISH ? "Registrera nytt konto" : "Register new account"));
            System.out.println("3. " + (language == Language.SWEDISH ? "Avsluta" : "Exit"));

            int userInput;
            try {
                userInput = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println((language == Language.SWEDISH ? "Ogiltigt val. Försök igen." : "Invalid choice. Please try again."));
                scanner.nextLine();
                continue;
            }

            switch (userInput) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.out.println((language == Language.SWEDISH ? "Avslutar programmet..." : "Exiting the program..."));
                    return;
                default:
                    System.out.println((language == Language.SWEDISH ? "Ogiltigt val. Försök igen." : "Invalid choice. Please try again."));
                    break;
            }
        }
    }

    private void login() {
        System.out.println((language == Language.SWEDISH ? "Logga in" : "Login"));
        System.out.println((language == Language.SWEDISH ? "Ange ditt användarnamn:" : "Enter your username:"));
        String enteredUsername = scanner.next();
        System.out.println((language == Language.SWEDISH ? "Ange ditt lösenord:" : "Enter your password:"));
        String enteredPassword = scanner.next();

        try {
            loggedInUser = userRepository.findByUsernameAndPassword(enteredUsername, enteredPassword);

            if (loggedInUser == null) {
                throw new Exception();
            }

            System.out.println((language == Language.SWEDISH ? "Inloggning lyckades. Välkommen, " : "Login successful. Welcome, ") + loggedInUser.getPlayerName() + "!");
            displayMenu();
        } catch (Exception e) {
            System.out.println((language == Language.SWEDISH ? "Inloggningen misslyckades. Försök igen." : "Login failed. Please try again."));
        }
    }

    private void register() {
        System.out.println((language == Language.SWEDISH ? "Registrera nytt konto" : "Register new account"));
        System.out.println((language == Language.SWEDISH ? "Ange önskat användarnamn:" : "Enter desired username:"));
        String username = scanner.next();
        System.out.println((language == Language.SWEDISH ? "Ange önskat lösenord:" : "Enter desired password:"));
        String password = scanner.next();
        System.out.println((language == Language.SWEDISH ? "Ange spelarnamn:" : "Enter player name:"));
        String playerName = scanner.next();

        try {
            register(username, password, playerName);
        } catch (Exception e) {
            System.out.println((language == Language.SWEDISH ? "Registreringen misslyckades. Försök igen." : "Registration failed. Please try again."));
        }
    }

    private void register(String username, String password, String playerName) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            System.out.println((language == Language.SWEDISH ? "Användarnamnet är redan taget. Välj ett annat" : "The username is already taken. Choose another one."));
            return;
        }

        User newUser = new User(username, password, playerName);
        userRepository.save(newUser);
        System.out.println((language == Language.SWEDISH ? "Användaren registrerades" : "User registered successfully."));
        getMenuOptions();
    }

    private void displayRankings() {
        List<User> users = userRepository.findAll();
        RankingManager.showRankings(users);
    }

    private void chooseLanguage() {
        language = null;
        System.out.println((language == Language.SWEDISH ? "Välj språk (svenska/engelska):" : "Choose language (svenska/engelska):"));
        System.out.println("1. Svenska");
        System.out.println("2. English");

        String languageChoice = scanner.next().toLowerCase();

        switch (languageChoice) {
            case "svenska", "1":
                language = Language.SWEDISH;
                System.out.println((language == Language.SWEDISH ? "Svenska valt." : "Swedish chosen."));
                break;
            case "english", "2":
                language = Language.ENGLISH;
                System.out.println((language == Language.SWEDISH ? "Engelska valt." : "English chosen."));
                break;
            default:
                System.out.println((language == Language.SWEDISH ? "Ogiltigt val. Standard till svenska!" : "Invalid choice. Defaulting to Swedish!"));
                language = Language.SWEDISH;
        }
    }

    public void displayMenu() {
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }

        while (true) {
            System.out.println((language == Language.SWEDISH ? "Alternativ:" : "Options:"));
            List<String> menuOptions = getMenuOptions();

            for (String menuOption : menuOptions) {
                System.out.println(menuOption);
            }

            int userInput;
            try {
                userInput = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println(getInvalidChoiceMessage());
                scanner.nextLine();
                continue;
            }

            switch (userInput) {
                case 1:
                    startChallenge();
                    break;
                case 2:
                    displayRankings();
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    logoutUser();
                    return;
                case 6:
                default:
                    System.out.println(getInvalidChoiceMessage());
                    break;
            }
        }
    }

    public List<String> getMenuOptions() {
        List<String> options = new ArrayList<>();
        options.add("1. " + (language == Language.SWEDISH ? "Spela" : "Play"));
        options.add("2. " + (language == Language.SWEDISH ? "Rankning" : "Ranking"));
        options.add("3. " + (language == Language.SWEDISH ? "Hantera konto" : "Manage account"));
        options.add("4. " + (language == Language.SWEDISH ? "Inställningar" : "Settings"));
        options.add("5. " + (language == Language.SWEDISH ? "Logga ut" : "Logout"));

        return options;
    }

    private String getInvalidChoiceMessage() {
        return switch (language) {
            case SWEDISH -> "Ogiltigt val. Välj mellan 1-5.";
            default -> "Invalid choice. Choose between 1-5.";
        };
    }

    private void logoutUser() {
        System.out.println((language == Language.SWEDISH ? "Utloggning lyckades. Hej då, " : "Logout successful. Goodbye, ") + (loggedInUser != null ? loggedInUser.getPlayerName() : "") + "!");
        loggedInUser = null;
    }

    public void startChallenge() {
        Challenge challenge = new Challenge(entityManager, rankingManager, this, scanner);
        loggedInUser = challenge.startChallenge(language, loggedInUser);
    }

    public enum Language {
        ENGLISH,
        SWEDISH
    }
}
