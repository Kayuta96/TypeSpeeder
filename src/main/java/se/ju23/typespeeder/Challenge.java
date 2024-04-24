package se.ju23.typespeeder;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

@Service
public class Challenge {
    private static final Random random = new Random();
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String SPECIAL_CHARACTERS = "@&?#";
    private static final int LETTERS_PER_WORD = 5;

    private final EntityManager entityManager;
    private final RankingManager rankingManager;
    private final Menu menu;
    private final Scanner scanner;

    @Autowired
    public Challenge(EntityManager entityManager, RankingManager rankingManager, Menu menu, Scanner scanner) {
        this.entityManager = entityManager;
        this.rankingManager = rankingManager;
        this.menu = menu;
        this.scanner = scanner;
    }

    public User startChallenge(Menu.Language language, User loggedInUser) {
        System.out.println((language == Menu.Language.SWEDISH ? "Välj en utmaningstyp:" : "Select a challenge type:"));
        System.out.println("1. " + (language == Menu.Language.SWEDISH ? "Bokstavsuutmaning" : "Letters Challenge"));
        System.out.println("2. " + (language == Menu.Language.SWEDISH ? "Orduutmaning" : "Words Challenge"));
        System.out.println("3. " + (language == Menu.Language.SWEDISH ? "Särskilda tecken utmaning" : "Special Characters Challenge"));

        int challengeType = scanner.nextInt();
        scanner.nextLine();

        switch (challengeType) {
            case 1:
                return lettersToType(generateRandomLetters(50), language, loggedInUser);
            case 2:
                return lettersToType(generateRandomWords(5), language, loggedInUser);
            case 3:
                return lettersToType(generateTextWithSpecialCharacters(), language, loggedInUser);
            default:
                System.out.println((language == Menu.Language.SWEDISH ? "Ogiltig utmaningstyp. Avslutar..." : "Invalid challenge type. Exiting..."));
                return loggedInUser;
        }
    }

    private User lettersToType(String text, Menu.Language language, User loggedInUser) {
        if (loggedInUser == null) {
            return null;
        }

        System.out.println("LettersToType - LoggedInUser: " + (loggedInUser != null ? loggedInUser.getUsername() : "None"));
        System.out.println((language == Menu.Language.SWEDISH ? "Skriv följande så snabbt du kan:" : "Type the following as fast as you can:"));
        System.out.println(text);

        long startTime = System.nanoTime();
        String userTypedText = scanner.nextLine();
        long endTime = System.nanoTime();

        boolean typedCorrectly = userTypedText.equals(text);
        System.out.println(language == Menu.Language.SWEDISH ? "Grattis! Du skrev det korrekt." : "Congratulations! You typed it correctly.");

        double totalTimeInSeconds = (endTime - startTime) / 1_000_000_000.0; // Konvertera nanosekunder till sekunder
        int wordsPerMinute = calculateWordsPerMinute(text.length(), totalTimeInSeconds);
        System.out.println((language == Menu.Language.SWEDISH ? "Tid tagen: " : "Time taken: ") + totalTimeInSeconds + " seconds.");
        System.out.println((language == Menu.Language.SWEDISH ? "Ord per minut: " : "Words per minute: ") + wordsPerMinute);

        int consecutiveCorrectCount = calculateConsecutiveCorrectCount(userTypedText, text);
        updateUserStatistics(loggedInUser, wordsPerMinute, text.length(), consecutiveCorrectCount, totalTimeInSeconds);

        if (loggedInUser != null) {
            updateRankings();
        }

        menu.displayMenu();
        return loggedInUser;
    }

    @Transactional
    public void updateUserStatistics(User user, int wordsPerMinute, int correctCount, int consecutiveCorrectCount, double totalTimeInSeconds) {
        UserStatistics userStatistics = user.getUserStatistics();
        if (userStatistics == null) {
            userStatistics = new UserStatistics();
            userStatistics.setUser(user);
            user.setUserStatistics(userStatistics);
        }
        userStatistics.updateSpeed(wordsPerMinute, totalTimeInSeconds);
        userStatistics.setCorrectCount(userStatistics.getCorrectCount() + correctCount);
        userStatistics.setConsecutiveCorrectCount(userStatistics.getConsecutiveCorrectCount() + consecutiveCorrectCount);
        entityManager.merge(userStatistics);
    }

    private int calculateWordsPerMinute(int textLength, double totalTimeInSeconds) {
        int words = textLength / LETTERS_PER_WORD;
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

    public int calculateConsecutiveCorrectCount(String userTypedText, String challengeText) {
        int consecutiveCorrectCount = 0;
        int minLength = Math.min(userTypedText.length(), challengeText.length());
        for (int i = 0; i < minLength; i++) {
            if (userTypedText.charAt(i) == challengeText.charAt(i)) {
                consecutiveCorrectCount++;
            } else {
                break;
            }
        }
        return consecutiveCorrectCount;
    }

    private void updateRankings() {
        List<User> users = entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
        rankingManager.showRankings(users);
    }

    public void lettersToType() {
    }

    public void startChallenge() {
    }
}
