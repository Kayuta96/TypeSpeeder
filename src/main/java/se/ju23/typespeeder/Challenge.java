package se.ju23.typespeeder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        this.entityManager = entityManager;
    }

    public Challenge() {}

    public void startChallenge(Menu.Language language, User loggedInUser) {
        Scanner scanner = new Scanner(System.in);

        System.out.println((language == Menu.Language.SWEDISH ? "Välj en utmaningstyp:" : "Select a challenge type:"));
        System.out.println("1. " + (language == Menu.Language.SWEDISH ? "Bokstavsuutmaning" : "Letters Challenge"));
        System.out.println("2. " + (language == Menu.Language.SWEDISH ? "Orduutmaning" : "Words Challenge"));
        System.out.println("3. " + (language == Menu.Language.SWEDISH ? "Särskilda tecken utmaning" : "Special Characters Challenge"));

        int challengeType = scanner.nextInt();
        scanner.nextLine();

        switch (challengeType) {
            case 1:
                lettersToType(generateRandomLetters(50), language, loggedInUser);
                break;
            case 2:
                lettersToType(generateRandomWords(5), language, loggedInUser);
                break;
            case 3:
                lettersToType(generateTextWithSpecialCharacters(), language, loggedInUser);
                break;
            default:
                System.out.println((language == Menu.Language.SWEDISH ? "Ogiltig utmaningstyp. Avslutar..." : "Invalid challenge type. Exiting..."));
        }
    }

    private void lettersToType(String text, Menu.Language language, User loggedInUser) {
        System.out.println((language == Menu.Language.SWEDISH ? "Skriv följande så snabbt du kan:" : "Type the following as fast as you can:"));
        System.out.println(text);

        long startTime = System.nanoTime();
        String userTypedText = getUserInput();
        long endTime = System.nanoTime();

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

        // Uppdatera användarens statistik med hastighet efter utmaningen
        loggedInUser.getUserStatistics().updateSpeed(wordsPerMinute, totalTimeInSeconds);
    }

    // Metod för att uppdatera användarstatistiken
    @Transactional
    public void updateStatistics(User user, int wordsPerMinute, int correctCount, int consecutiveCorrectCount) {
        UserStatistics userStatistics = user.getUserStatistics();
        if (userStatistics == null) {
            userStatistics = new UserStatistics();
            userStatistics.setUser(user);
            user.setUserStatistics(userStatistics);
        }
        double totalTimeInSeconds = 0;
        userStatistics.updateSpeed(wordsPerMinute, totalTimeInSeconds); // totalTimeInSeconds behöver tillhandahållas
        userStatistics.setCorrectCount(userStatistics.getCorrectCount() + correctCount);
        userStatistics.setConsecutiveCorrectCount(userStatistics.getConsecutiveCorrectCount() + consecutiveCorrectCount);
        entityManager.merge(userStatistics);
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
            char currentChar = random.nextBoolean() ? SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())) : (char) (random.nextInt(26) + 'a');
            text.append(currentChar);
        }
        return text.toString();
    }

    private String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public void lettersToType() {
    }

    public void startChallenge() {
    }
}