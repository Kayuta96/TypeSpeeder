package se.ju23.typespeeder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


import java.util.Random;
import java.util.Scanner;

@Service
public class Challenge {
    private static final Random random = new Random();
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String SPECIAL_CHARACTERS = "@&?#";

    @PersistenceContext
    private EntityManager entityManager;

    public Challenge(EntityManager entityManager) {
    }

    public Challenge() {
    }

    public void startChallenge(Menu.Language language, User loggedInUser) {
        Scanner scanner = new Scanner(System.in);

        System.out.println((language == Menu.Language.SWEDISH ? "Välj en utmaningstyp:" : "Select a challenge type:"));
        System.out.println("1. " + (language == Menu.Language.SWEDISH ? "Bokstavsutmaning" : "Letters Challenge"));
        System.out.println("2. " + (language == Menu.Language.SWEDISH ? "Orduutmaning" : "Words Challenge"));
        System.out.println("3. " + (language == Menu.Language.SWEDISH ? "Särskilda tecken utmaning" : "Special Characters Challenge"));

        int challengeType = scanner.nextInt();
        scanner.nextLine();

        switch (challengeType) {
            case 1:
                lettersToType(generateRandomLetters(20), language, loggedInUser);
                break;
            case 2:
                lettersToType(generateRandomWords(5, language), language, loggedInUser);
                break;
            case 3:
                lettersToType(generateTextWithSpecialCharacters(language), language, loggedInUser);
                break;
            default:
                System.out.println((language == Menu.Language.SWEDISH ? "Ogiltig utmaningstyp. Återgår till huvudmenyn..." : "Invalid challenge type. Returning to main menu..."));
                break;
        }
    }

    private void lettersToType(String text, Menu.Language language, User loggedInUser) {
        System.out.println((language == Menu.Language.SWEDISH ? "Skriv följande så snabbt du kan:" : "Type the following as fast as you can:"));
        System.out.println(text);

        long startTime = System.nanoTime();
        String userTypedText = getUserInput();
        long endTime = System.nanoTime();

        int correct = 0;
        int inOrderCorrect = 0;
        boolean isCorrect = true;
        for (int i = 0; i < text.length(); i++) {
            if (i < userTypedText.length() && text.charAt(i) == userTypedText.charAt(i)) {
                correct++;
                if (isCorrect) {
                    inOrderCorrect++;
                }
            } else {
                isCorrect = false;
            }
        }

        while (!userTypedText.equals(text)) {
            System.out.println((language == Menu.Language.SWEDISH ? "Felaktigt. Skriv om hela utmaningen:" : "Incorrect. Retype the entire challenge:"));
            userTypedText = getUserInput();
        }

        boolean typedCorrectly = userTypedText.equals(text);
        System.out.println(typedCorrectly ? (language == Menu.Language.SWEDISH ? "Grattis! Du skrev det korrekt." : "Congratulations! You typed it correctly.") : (language == Menu.Language.SWEDISH ? "Utmaningen klarades med fel." : "Challenge completed with errors."));
        long totalTime = endTime - startTime;
        double totalTimeInSeconds = totalTime / 1_000_000_000.0; // Konvertera nanosekunder till sekunder
        int wordsPerMinute = calculateWordsPerMinute(text.length(), totalTimeInSeconds);
        System.out.println((language == Menu.Language.SWEDISH ? "Tid tagen: " : "Time taken: ") + totalTimeInSeconds + " seconds.");
        System.out.println((language == Menu.Language.SWEDISH ? "Ord per minut: " : "Words per minute: ") + wordsPerMinute);

    }

    private int calculateWordsPerMinute(int textLength, double totalTimeInSeconds) {
        // Anta att varje ord är 5 tecken långt för enkelhetens skull
        int words = textLength / 5;
        // Beräkna antalet ord per minut
        return (int) (words / totalTimeInSeconds * 60);
    }

    private String generateRandomLetters(int count) {
        StringBuilder randomLetters = new StringBuilder();
        for (int i = 0; i < count; i++) {
            randomLetters.append(LETTERS.charAt(random.nextInt(LETTERS.length())));
        }
        return randomLetters.toString();
    }

    private String generateRandomWords(int wordCount, Menu.Language language) {
        StringBuilder randomWords = new StringBuilder();
        List<String> words = (language == Menu.Language.SWEDISH) ? SWEDISH_WORDS : ENGLISH_WORDS;

        for (int i = 0; i < wordCount; i++) {
            String randomWord = words.get(random.nextInt(words.size()));
            randomWords.append(randomWord).append(" ");
        }
        return randomWords.toString().trim();
    }

    private String generateTextWithSpecialCharacters(Menu.Language language) {
        StringBuilder text = new StringBuilder();
        List<String> words = (language == Menu.Language.SWEDISH) ? SWEDISH_WORDS : ENGLISH_WORDS;

        int textLength = random.nextInt(5) + 5;

        for (int i = 0; i < textLength; i++) {
            // Välj ett slumpmässigt ord
            String randomWord = words.get(random.nextInt(words.size()));
            text.append(randomWord);

            if (random.nextBoolean()) {
                text.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));
            }

            if (i < textLength - 1) {
                text.append(" ");
            }
        }

        return text.toString();
    }

    private String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private String getLocalizedText(Menu.Language language, String swedish, String english) {
        return language == Menu.Language.SWEDISH ? swedish : english;
    }


    private static final List<String> SWEDISH_WORDS = Arrays.asList(
            "hund", "katt", "bil", "bok", "blomma", "träd", "hus", "stjärna", "sjö", "fisk"
    );

    private static final List<String> ENGLISH_WORDS = Arrays.asList(
            "dog", "cat", "car", "book", "flower", "tree", "house", "star", "lake", "fish"
    );

    public void startChallenge() {
    }

    public void lettersToType() {
    }
}