package se.ju23.typespeeder;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Menu implements MenuService {
    private Language language;

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.displayMenu();
    }

    public Menu() {
        chooseLanguage();
    }

    private void chooseLanguage() {  //Språk metod
        Scanner scanner = new Scanner(System.in);
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
                break;
        }
    }

    @Override
    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);

        String[] menuChoices;
        switch (language) {
            case SWEDISH:
                menuChoices = new String[]{
                        "1. Spela",
                        "2. Ranking",
                        "3. Hantera konto",
                        "4. Inställningar",
                        "5. Logga ut"
                };
                break;
            case ENGLISH:
            default:
                menuChoices = new String[]{
                        "1. Play",
                        "2. Ranking",
                        "3. Manage account",
                        "4. Settings",
                        "5. Logout"
                };
                break;
        }

        while (true) {
            System.out.println("------------");
            System.out.println(getWelcomeMessage());
            for (String menuChoice : menuChoices)
                System.out.println(menuChoice);

            int userInput = scanner.nextInt();

            switch (userInput) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    break;
                default:
                    System.out.println(getInvalidChoiceMessage());
                    break;
            }
        }
    }

    private String getWelcomeMessage() {
        switch(language) {
            case SWEDISH:
                return "Välkommen till huvudmenyn."
            case ENGLISH:
            default:
                return "Welcome to the main menu."
        }
    }

    private String getInvalidChoiceMessage() {
        switch (language) {
            case SWEDISH:
                return "Ogiltigt val. Välj mellan 1-5.";
            case ENGLISH:
            default:
                return "Invalid choice. Choose between 1-5.";
        }
    }

    @Override
    public List<String> getMenuOptions() {
        return null;
    }
    private enum Language {
        ENGLISH,
        SWEDISH
    }


}
