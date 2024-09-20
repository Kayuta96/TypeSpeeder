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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;
    private RankingService rankingService;
    private User loggedInUser;
    private Language language;
    private Scanner scanner;

    @Autowired
    private UserStatisticsRepository userStatisticsRepository;

    @Autowired
    private UserService userService;

    @Autowired
    public Menu(UserRepository userRepository, EntityManager entityManager, RankingService rankingService) {
        this.userRepository = userRepository;
        this.entityManager = entityManager;
        this.rankingService = rankingService;
        this.scanner = new Scanner(System.in);
    }

    public Menu() {
        this.scanner = new Scanner(System.in);
    }

    public Scanner getScanner() {
        return this.scanner;
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
                    System.out.println(getLocalizedText(language, "Exiting the program...", "Avslutar programmet..."));
                    return;
                }
                default -> System.out.println(getLocalizedText(language, "Invalid choice. Please try again.", "Ogiltigt val. Försök igen."));
            }
        }
    }

    private void login() {
        System.out.println(getLocalizedText(language, "Login", "Logga in"));
        System.out.print(getLocalizedText(language, "Enter your username: ", "Ange ditt användarnamn:"));
        String enteredUsername = scanner.next();
        System.out.print(getLocalizedText(language, "Enter your password: ", "Ange ditt lösenord:"));
        String enteredPassword = scanner.next();

        try {
            loggedInUser = userRepository.findByUsernameAndPassword(enteredUsername, enteredPassword);

            if (loggedInUser == null) {
                throw new Exception();
            }

            loggedInUser.getPlayerName();

            System.out.println(getLocalizedText(language, "Login successful. Welcome, ", "Inloggning lyckades. Välkommen, ") + loggedInUser.getPlayerName() + "!");
            displayMenu(loggedInUser);
        } catch (Exception e) {
            System.out.println(getLocalizedText(language, "Login failed. Please try again.", "Inloggningen misslyckades. Försök igen."));
        }
    }

    private void register() {
        System.out.println(getLocalizedText(language, "Register new account", "Registrera nytt konto"));
        System.out.print(getLocalizedText(language, "Enter desired username: ", "Ange önskat användarnamn:"));
        String username = scanner.next();
        System.out.print(getLocalizedText(language, "Enter desired password: ", "Ange önskat lösenord:"));
        String password = scanner.next();
        System.out.print(getLocalizedText(language, "Enter player name: ", "Ange spelarnamn:"));
        String playerName = scanner.next();

        try {
            registerNewUser(username, password, playerName);
        } catch (Exception e) {
            System.out.println(getLocalizedText(language, "Registration failed. Please try again.", "Registreringen misslyckades. Försök igen."));
        }
    }

    private void registerNewUser(String username, String password, String playerName) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            System.out.println(getLocalizedText(language, "The username is already taken. Choose another one.", "Användarnamnet är redan taget. Välj ett annat"));
            return;
        }

        User newUser = new User(username, password, playerName);
        userRepository.save(newUser);
        System.out.println(getLocalizedText(language, "User registered successfully.", "Användaren registrerades"));
        displayMenu(loggedInUser);
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
        System.out.println(getLocalizedText(language, "Welcome to the menu!", "Välkommen till menyn!"));
        System.out.println(getLocalizedText(language, "What would you like to do?", "Vad vill du göra?"));
        System.out.println("1. " + getLocalizedText(language, "Login", "Logga in"));
        System.out.println("2. " + getLocalizedText(language, "Register new account", "Registrera nytt konto"));
        System.out.println("3. " + getLocalizedText(language, "Exit", "Avsluta"));
    }

    private int getUserInput() {
        try {
            int userInput = scanner.nextInt();
            scanner.nextLine();
            return userInput;
        } catch (InputMismatchException e) {
            System.out.println(getLocalizedText(language, "Invalid choice. Please try again.", "Ogiltigt val. Försök igen."));
            scanner.nextLine();
            return -1;
        }
    }

    public String getLocalizedText(Language language, String englishText, String swedishText) {
        return language == Language.SWEDISH ? swedishText : englishText;
    }

    @Override
    public void displayMenu(User loggedInUser) {
        while (true) {
            System.out.println(getLocalizedText(language, "Options:", "Alternativ:"));
            List<String> menuOptions = getMenuOptions();

            for (String menuOption : menuOptions) {
                System.out.println(menuOption);
            }

            int userInput = getUserInput();
            if (userInput == -1) continue;

            switch (userInput) {
                case 1 -> startChallenge();
                case 2 -> displayRanking();
                case 3 -> manageAccount();
                case 4 -> settings();
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
        options.add("1. " + getLocalizedText(language, "Play", "Spela"));
        options.add("2. " + getLocalizedText(language, "Ranking", "Rankning"));
        options.add("3. " + getLocalizedText(language, "Manage account", "Hantera konto"));
        options.add("4. " + getLocalizedText(language, "Settings", "Inställningar"));
        options.add("5. " + getLocalizedText(language, "Logout", "Logga ut"));
        return options;
    }

    private String getInvalidChoiceMessage() {
        return getLocalizedText(language, "Invalid choice. Choose between 1-5.", "Ogiltigt val. Välj mellan 1-5.");
    }

    private void logoutUser() {
        System.out.println(getLocalizedText(language, "Logout successful. Goodbye, ", "Utloggning lyckades. Hej då, ") + (loggedInUser != null ? loggedInUser.getPlayerName() : "") + "!");
        loggedInUser = null;
    }

    public void startChallenge() {
        if (loggedInUser == null) {
            System.out.println("User is not logged in. Cannot start challenge.");
            return;
        }

        // Pass all required dependencies to Challenge
        Challenge challenge = new Challenge(entityManager, userRepository, this, userService);
        challenge.startChallenge(language, loggedInUser);
    }

    private void displayRanking() {
        // Fetcha användarna med bästa WPM
        List<UserStatistics> rankedUsers = userStatisticsRepository.findAllByOrderByBestWpmDesc();
        System.out.println(getLocalizedText(language, "Ranking List (by WPM):", "Rankninglista (efter WPM):"));
        int rank = 1;
        boolean userIncluded = false;

        // Display WPM för alla
        for (UserStatistics stats : rankedUsers) {
            User user = stats.getUser();
            if (user.equals(loggedInUser)) {
                userIncluded = true;
            }
            System.out.println(rank + ". " + user.getPlayerName() + " - WPM: " + stats.getBestWpm());
            rank++;
        }

        // Om det vore att någon är inte inloggad berätta så
        if (!userIncluded) {
            UserStatistics userStats = userStatisticsRepository.findByUser(loggedInUser);
            if (userStats != null) {
                System.out.println("N/A. " + loggedInUser.getPlayerName() + " - WPM: " + userStats.getBestWpm());
            } else {
                System.out.println("N/A. " + loggedInUser.getPlayerName() + " - WPM: " + getLocalizedText(language, "No statistics available", "Inga statistik tillgänglig"));
            }
        }
    }

    private void manageAccount() {
        System.out.println(getLocalizedText(language, "Manage Account", "Hantera konto"));
        System.out.println("1. " + getLocalizedText(language, "Change Name", "Ändra namn"));
        System.out.println("2. " + getLocalizedText(language, "Change Password", "Ändra lösenord"));
        System.out.println("3. " + getLocalizedText(language, "Back to Menu", "Tillbaka till menyn"));

        int choice = getUserInput();
        if (choice == -1) return;

        switch (choice) {
            case 1 -> changeName();
            case 2 -> changePassword();
            case 3 -> displayMenu(loggedInUser);
            default -> System.out.println(getInvalidChoiceMessage());
        }
    }

    private void changeName() {
        System.out.print(getLocalizedText(language, "Enter new player name: ", "Ange nytt spelarnamn:"));
        String newPlayerName = scanner.next();
        loggedInUser.setPlayerName(newPlayerName);
        userRepository.save(loggedInUser);
        System.out.println(getLocalizedText(language, "Player name updated successfully.", "Spelarnamnet har uppdaterats."));
    }

    private void changePassword() {
        System.out.print(getLocalizedText(language, "Enter new password: ", "Ange nytt lösenord:"));
        String newPassword = scanner.next();
        loggedInUser.setPassword(newPassword);
        userRepository.save(loggedInUser);
        System.out.println(getLocalizedText(language, "Password updated successfully.", "Lösenordet har uppdaterats."));
    }

    private void settings() {
        System.out.println(getLocalizedText(language, "Settings", "Inställningar"));
        System.out.println("1. " + getLocalizedText(language, "Change Language", "Ändra språk"));
        System.out.println("2. " + getLocalizedText(language, "Back to Menu", "Tillbaka till menyn"));

        int choice = getUserInput();
        if (choice == -1) return;

        switch (choice) {
            case 1 -> {
                chooseLanguage();
                System.out.println(getLocalizedText(language, "Language changed successfully.", "Språket har ändrats."));
            }
            case 2 -> displayMenu(loggedInUser);
            default -> System.out.println(getInvalidChoiceMessage());
        }
    }

    public enum Language {
        ENGLISH,
        SWEDISH
    }
}