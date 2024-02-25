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
        String[] wordsToType = generateWords(10);
        StringBuilder[] highlightedWords = highlightWords(wordsToType);

        System.out.println("Type the following words as fast as you can:");

        long startTime = System.currentTimeMillis();
        int failedAttempts = 0;

        for (int i = 0; i < wordsToType.length; i++) {
            boolean typedCorrectly = false;
            do {
                System.out.print(highlightedWords[i] + " ");
                String userTypedWord = getUserInput();

                if (userTypedWord.equals(wordsToType[i])) {
                    typedCorrectly = true;
                } else {
                    System.out.println("Incorrect! Retype the word.");
                    failedAttempts++;
                }
            } while (!typedCorrectly);
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        int wordsPerMinute = calculateWordsPerMinute(wordsToType.length, totalTime);
        System.out.println("Congratulations! You completed the challenge.");
        System.out.println("Your typing speed: " + wordsPerMinute + " words per minute.");
        System.out.println("Number of failed attempts: " + failedAttempts);
    }

    private StringBuilder[] highlightWords(String[] words) {
        StringBuilder[] highlightedWords = new StringBuilder[words.length];
        for (int i = 0; i < words.length; i++) {
            highlightedWords[i] = new StringBuilder();
            for (char c : words[i].toCharArray()) {
                if (random.nextBoolean()) {
                    highlightedWords[i].append(ANSI_RED).append(c).append(ANSI_RESET);
                } else {
                    highlightedWords[i].append(ANSI_GREEN).append(c).append(ANSI_RESET);
                }
            }
        }
        return highlightedWords;
    }

    private String[] generateWords(int wordCount) {
        String[] words = new String[wordCount];
        for (int i = 0; i < wordCount; i++) {
            words[i] = dictionary.get(random.nextInt(dictionary.size()));
        }
        return words;
    }

    private String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    private int calculateWordsPerMinute(int wordsTyped, long totalTimeMillis) {
        int wordsPerMinute = (int) ((wordsTyped / (double) totalTimeMillis) * 60000);
        return wordsPerMinute;
    }
}