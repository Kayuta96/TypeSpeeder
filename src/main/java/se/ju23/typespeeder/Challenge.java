package se.ju23.typespeeder;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Challenge {
    private static final Random random = new Random();
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String SPECIAL_CHARACTERS = "@&?#";

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";

    public void startChallenge() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Select a challenge type:");
        System.out.println("1. Letters Challenge");
        System.out.println("2. Words Challenge");
        System.out.println("3. Special Characters Challenge");

        int challengeType = scanner.nextInt();
        scanner.nextLine();

        switch (challengeType) {
            case 1:
                lettersToType(generateRandomLetters(50));
                break;
            case 2:
                lettersToType(generateRandomWords(5));
                break;
            case 3:
                lettersToType(generateTextWithSpecialCharacters());
                break;
            default:
                System.out.println("Invalid challenge type. Exiting...");
        }
    }

    private void lettersToType(String text) {
        String highlightedText = highlightText(text);

        System.out.println("Type the following as fast as you can:");
        System.out.println(highlightedText);

        long startTime = System.nanoTime();
        String userTypedText = getUserInput();
        long endTime = System.nanoTime();

        int failedAttempts = 0;

        while (!userTypedText.equals(text)) {
            System.out.println("Incorrect. Retype the entire challenge:");

            startTime = System.nanoTime();
            userTypedText = getUserInput();
            endTime = System.nanoTime();

            failedAttempts++;
        }

        boolean typedCorrectly = userTypedText.equals(text);
        System.out.println(typedCorrectly ? "Congratulations! You typed it correctly." : "Challenge completed with errors.");
        printResult(text.length(), startTime, endTime, failedAttempts);
    }

    private String generateRandomLetters(int count) {
        StringBuilder randomLetters = new StringBuilder();
        for (int i = 0; i < count; i++) {
            randomLetters.append(LETTERS.charAt(random.nextInt(LETTERS.length())));
        }
        return randomLetters.toString();
    }

    private String generateRandomWords(int wordCount) {
        StringBuilder randomWords = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            randomWords.append(WordRepository.getRandomWord()).append(" ");
        }
        return randomWords.toString().trim();
    }

    private String generateTextWithSpecialCharacters() {
        StringBuilder text = new StringBuilder();
        int textLength = random.nextInt(50) + 50;
        for (int i = 0; i < textLength; i++) {
            char currentChar;
            if (random.nextBoolean()) {
                currentChar = SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length()));
            } else {
                currentChar = (char) (random.nextInt(26) + 'a');
            }
            text.append(currentChar);
        }
        return text.toString();
    }

    private String highlightText(String text) {
        StringBuilder highlightedText = new StringBuilder();
        for (char c : text.toCharArray()) {
            highlightedText.append(random.nextBoolean() ? ANSI_RED : ANSI_GREEN).append(c).append(ANSI_RESET);
        }
        return highlightedText.toString();
    }

    private String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private void printResult(int inputLength, long startTime, long endTime, int failedAttempts) {
        long totalTime = endTime - startTime;
        int inputPerMinute = (int) ((inputLength / (double) totalTime) * 60000000000L);
        System.out.println("Your typing speed: " + inputPerMinute + " per minute.");
        System.out.println("Number of failed attempts: " + failedAttempts);
    }

    public void lettersToType() {
    }
}