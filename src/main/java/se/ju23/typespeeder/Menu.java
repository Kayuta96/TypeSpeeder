package se.ju23.typespeeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

@Service
public class Menu implements MenuService {
    private final UserRepository userRepository;
    private User loggedInUser;
    private Language language;
    private final Scanner scanner;

    @Autowired
    public Menu(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.loggedInUser = null;
        this.scanner = new Scanner(System.in);
        chooseLanguage();
    }

    public void start() {
        while (loggedInUser == null) {
            System.out.println(getWelcomeMessage());
            System.out.println("Enter your username:");
            String enteredUsername = scanner.next();
            System.out.println("Enter your password:");
            String enteredPassword = scanner.next();

            loggedInUser = userRepository.findByUsernameAndPassword(enteredUsername, enteredPassword);

            if (loggedInUser == null) {
                System.out.println("Login failed. Please try again.");
            } else {
                System.out.println("Login successful. Welcome, " + loggedInUser.getPlayerName() + "!");
            }
        }

        while (true) {
            System.out.println("------------");
            System.out.println(getWelcomeMessage());
            displayMenu();

            int userInput = scanner.nextInt();

            switch (userInput) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    logoutUser();
                    return;
                default:
                    System.out.println(getInvalidChoiceMessage());
                    break;
            }
        }
    }

    private void chooseLanguage() {
        System.out.println("Choose language / Välj språk:");
        System.out.println("1. English");
        System.out.println("2. Svenska");

        int languageChoice = scanner.nextInt();

        switch (languageChoice) {
            case 1:
                language = Language.ENGLISH;
                break;
            case 2:
                language = Language.SWEDISH;
                break;
            default:
                System.out.println("Invalid choice. Will default to English!");
                language = Language.ENGLISH;
        }
    }

    @Override
    public void displayMenu() {
        System.out.println("Options:");
        List<String> menuOptions = getMenuOptions();
        for (String menuOption : menuOptions) {
            System.out.println(menuOption);
        }
    }

    @Override
    public List<String> getMenuOptions() {
        return switch (language) {
            case SWEDISH -> List.of(
                    "1. Spela",
                    "2. Ranking",
                    "3. Hantera konto",
                    "4. Inställningar",
                    "5. Logga ut"
            );
            default -> List.of(
                    "1. Play",
                    "2. Ranking",
                    "3. Manage account",
                    "4. Settings",
                    "5. Logout"
            );
        };
    }

    private String getWelcomeMessage() {
        return switch (language) {
            case SWEDISH -> "Välkommen till huvudmenyn, " + (loggedInUser != null ? loggedInUser.getPlayerName() : "") + "!";
            default -> "Welcome to the main menu, " + (loggedInUser != null ? loggedInUser.getPlayerName() : "") + "!";
        };
    }

    private String getInvalidChoiceMessage() {
        return switch (language) {
            case SWEDISH -> "Ogiltigt val. Välj mellan 1-5.";
            default -> "Invalid choice. Choose between 1-5.";
        };
    }

    private void logoutUser() {
        System.out.println("Logout successful. Goodbye, " + (loggedInUser != null ? loggedInUser.getPlayerName() : "") + "!");
        loggedInUser = null;
    }

    private enum Language {
        ENGLISH,
        SWEDISH
    }
}