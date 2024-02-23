package se.ju23.typespeeder;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Challenge {
    private static final Random random = new Random();
    private static final List<String> dictionary = List.of(
            "apple", "banana", "chocolate", "programming", "challenge", "keyboard", "algorithm", "java", "spring", "developer"
    );

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";

    public void startChallenge() {
        String challengeText = wordsToType(10);
        String highlightedChallenge = highlightText(challengeText);

        System.out.println("Type the following text as fast as you can:");
        System.out.println(highlightedChallenge);

        Scanner scanner = new Scanner(System.in);
        String userTypedText = scanner.nextLine();

    }

    private String highlightText(String text) {
        StringBuilder highlightedText = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (random.nextBoolean()) {
                highlightedText.append(ANSI_RED).append(c).append(ANSI_RESET);
            } else {
                highlightedText.append(ANSI_GREEN).append(c).append(ANSI_RESET);
            }
        }
        return highlightedText.toString();
    }

    private String wordsToType(int wordCount) {
        StringBuilder words = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            words.append(dictionary.get(random.nextInt(dictionary.size())));
            words.append(" ");
        }
        return words.toString().trim();
    }
}