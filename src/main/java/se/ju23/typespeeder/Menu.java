package se.ju23.typespeeder;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Service
public class Menu implements MenuService {
    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final RankingService rankingService;
    private User loggedInUser;
    private Language language;
    private final Scanner scanner;

    @Autowired
    public Menu(UserRepository userRepository, EntityManager entityManager, RankingService rankingService) {
        this.userRepository = userRepository;
        this.entityManager = entityManager;
        this.rankingService = rankingService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        chooseLanguage();
        while (true) {
            displayMainMenu();
            int userInput = getUserInput();
            if (userInput == -1) continue;

            switch (userInput) {
                case 1 -> login();
                case 2 -> register();
                case 3 -> {
                    System.out.println(getLocalizedText("Exiting the program...", "Avslutar programmet..."));
                    return;
                }
                default -> System.out.println(getLocalizedText("Invalid choice. Please try again.", "Ogiltigt val. Försök igen."));
            }
        }
    }

    private void login() {
        System.out.println(getLocalizedText("Login", "Logga in"));
        System.out.print(getLocalizedText("Enter your username: ", "Ange ditt användarnamn:"));
        String enteredUsername = scanner.next();
        System.out.print(getLocalizedText("Enter your password: ", "Ange ditt lösenord:"));
        String enteredPassword = scanner.next();

        try {
            loggedInUser = userRepository.findByUsernameAndPassword(enteredUsername, enteredPassword);

            if (loggedInUser == null) {
                throw new Exception();
            }

            System.out.println(getLocalizedText("Login successful. Welcome, ", "Inloggning lyckades. Välkommen, ") + loggedInUser.getPlayerName() + "!");
            displayMenu();
        } catch (Exception e) {
            System.out.println(getLocalizedText("Login failed. Please try again.", "Inloggningen misslyckades. Försök igen."));
        }
    }

    private void register() {
        System.out.println(getLocalizedText("Register new account", "Registrera nytt konto"));
        System.out.print(getLocalizedText("Enter desired username: ", "Ange önskat användarnamn:"));
        String username = scanner.next();
        System.out.print(getLocalizedText("Enter desired password: ", "Ange önskat lösenord:"));
        String password = scanner.next();
        System.out.print(getLocalizedText("Enter player name: ", "Ange spelarnamn:"));
        String playerName = scanner.next();

        try {
            registerNewUser(username, password, playerName);
        } catch (Exception e) {
            System.out.println(getLocalizedText("Registration failed. Please try again.", "Registreringen misslyckades. Försök igen."));
        }
    }

    private void registerNewUser(String username, String password, String playerName) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            System.out.println(getLocalizedText("The username is already taken. Choose another one.", "Användarnamnet är redan taget. Välj ett annat"));
            return;
        }

        User newUser = new User(username, password, playerName);
        userRepository.save(newUser);
        System.out.println(getLocalizedText("User registered successfully.", "Användaren registrerades"));
        displayMenu();
    }

    private void chooseLanguage() {
        language = null;
        System.out.println("Choose language (svenska/engelska):");
        System.out.println("1. Svenska");
        System.out.println("2. English");

        String languageChoice = scanner.next().toLowerCase();

        switch (languageChoice) {
            case "svenska", "1" -> {
                language = Language.SWEDISH;
                System.out.println("Svenska valt.");
            }
            case "english", "2" -> {
                language = Language.ENGLISH;
                System.out.println("English chosen.");
            }
            default -> {
                System.out.println("Ogiltigt val. Standard till svenska!");
                language = Language.SWEDISH;
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("------------");
        System.out.println(getLocalizedText("Welcome to the menu!", "Välkommen till menyn!"));
        System.out.println(getLocalizedText("What would you like to do?", "Vad vill du göra?"));
        System.out.println("1. " + getLocalizedText("Login", "Logga in"));
        System.out.println("2. " + getLocalizedText("Register new account", "Registrera nytt konto"));
        System.out.println("3. " + getLocalizedText("Exit", "Avsluta"));
    }

    private int getUserInput() {
        try {
            int userInput = scanner.nextInt();
            scanner.nextLine();
            return userInput;
        } catch (InputMismatchException e) {
            System.out.println(getLocalizedText("Invalid choice. Please try again.", "Ogiltigt val. Försök igen."));
            scanner.nextLine();
            return -1;
        }
    }

    private String getLocalizedText(String englishText, String swedishText) {
        return language == Language.SWEDISH ? swedishText : englishText;
    }

    @Override
    public void displayMenu() {
        while (true) {
            System.out.println(getLocalizedText("Options:", "Alternativ:"));
            List<String> menuOptions = getMenuOptions();

            for (String menuOption : menuOptions) {
                System.out.println(menuOption);
            }

            int userInput = getUserInput();
            if (userInput == -1) continue;

            switch (userInput) {
                case 1 -> startChallenge();
                case 2 -> displayRanking();
                case 3 -> {
                    // TODO: Implement account management
                }
                case 4 -> {
                    // TODO: Implement settings
                }
                case 5 -> {
                    logoutUser();
                    return;
                }
                default -> System.out.println(getInvalidChoiceMessage());
            }
        }
    }

    @Override
    public List<String> getMenuOptions() {
        List<String> options = new ArrayList<>();
        options.add("1. " + getLocalizedText("Play", "Spela"));
        options.add("2. " + getLocalizedText("Ranking", "Rankning"));
        options.add("3. " + getLocalizedText("Manage account", "Hantera konto"));
        options.add("4. " + getLocalizedText("Settings", "Inställningar"));
        options.add("5. " + getLocalizedText("Logout", "Logga ut"));
        return options;
    }

    private String getInvalidChoiceMessage() {
        return getLocalizedText("Invalid choice. Choose between 1-5.", "Ogiltigt val. Välj mellan 1-5.");
    }

    private void logoutUser() {
        System.out.println(getLocalizedText("Logout successful. Goodbye, ", "Utloggning lyckades. Hej då, ") + (loggedInUser != null ? loggedInUser.getPlayerName() : "") + "!");
        loggedInUser = null;
    }

    private void startChallenge() {
        Challenge challenge = new Challenge(entityManager);
        challenge.startChallenge(language, loggedInUser);
    }

    private void displayRanking() {
        List<User> rankedUsers = rankingService.getRankedUsers();
        System.out.println(getLocalizedText("Ranking List:", "Rankninglista:"));
        int rank = 1;
        for (User user : rankedUsers) {
            System.out.println(rank + ". " + user.getPlayerName() + " - " + rankingService.calculateCompositeScore(user));
            rank++;
        }
    }

    public enum Language {
        ENGLISH,
        SWEDISH
    }
}