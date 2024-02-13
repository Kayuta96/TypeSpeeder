package se.ju23.typespeeder;
import java.util.Scanner;

public class Menu implements MenuService {


    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.displayMenu();
    }

    @Override
    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);

        String[] menyChoices = {
                "1.",
                "2.",
                "3.",
                "4.",
                "5."
        };
        while (true) {
            System.out.println("------------");
            System.out.println("Välkommen till huvudmenyn.");
            for (String menyChoice : menyChoices)
                System.out.println(menyChoice);

            int userInput = scanner.nextInt();

            switch (userInput) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    break;
                default:
                    System.out.println("Invalid choice. Choose between 1-5.");
                    break;
            }
        }
    }
    @Override
    public List<String> getMenuOptions() {
        return null;
    }

}
