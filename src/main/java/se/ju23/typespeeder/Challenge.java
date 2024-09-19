package se.ju23.typespeeder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public Challenge() {
        
    }

    public void startChallenge(Menu.Language language, User loggedInUser) {
        if (loggedInUser == null) {
            System.out.println(getLocalizedText(language, "Du måste vara inloggad för att starta en utmaning.", "You need to be logged in to start a challenge."));
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println(getLocalizedText(language, "Välj en utmaningstyp:", "Select a challenge type:"));
        System.out.println("1. " + getLocalizedText(language, "Bokstavsutmaning", "Letters Challenge"));
        System.out.println("2. " + getLocalizedText(language, "Orduutmaning", "Words Challenge"));
        System.out.println("3. " + getLocalizedText(language, "Särskilda tecken utmaning", "Special Characters Challenge"));

        int challengeType = -1;
        try {
            challengeType = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println(getLocalizedText(language, "Ogiltig inmatning. Försök igen.", "Invalid input. Please try again."));
            scanner.nextLine(); 
            return;
        }

        switch (challengeType) {
            case 1:
                System.out.println(getLocalizedText(language, "Startar bokstavsutmaning...", "Starting letters challenge..."));
                lettersToType(generateRandomLetters(20, loggedInUser.getLevel()), language, loggedInUser);
                break;
            case 2:
                System.out.println(getLocalizedText(language, "Startar orduutmaning...", "Starting words challenge..."));
                lettersToType(generateRandomWords(5, language, loggedInUser.getLevel()), language, loggedInUser);
                break;
            case 3:
                System.out.println(getLocalizedText(language, "Startar särskilda tecken-utmaning...", "Starting special characters challenge..."));
                lettersToType(generateTextWithSpecialCharacters(language, loggedInUser.getLevel()), language, loggedInUser);
                break;
            default:
                System.out.println(getLocalizedText(language, "Ogiltig utmaningstyp. Återgår till huvudmenyn...", "Invalid challenge type. Returning to main menu..."));
                break;
        }

        System.out.println(getLocalizedText(language, "Utmaning avslutad!", "Challenge completed!"));
    }


    private void lettersToType(String text, Menu.Language language, User loggedInUser) {
        System.out.println(getLocalizedText(language, "Skriv följande så snabbt du kan:", "Type the following as fast as you can:"));
        System.out.println(text);

        long startTime = System.nanoTime();
        String userTypedText = getUserInput();
        long endTime = System.nanoTime();

        long totalTime = endTime - startTime;
        double totalTimeInSeconds = totalTime / 1_000_000_000.0;
        int wordsPerMinute = calculateWordsPerMinute(text.length(), totalTimeInSeconds);

        boolean typedCorrectly = userTypedText.equals(text);
        int basePoints = 20;

        if (typedCorrectly) {
            loggedInUser.addPoints(basePoints);
            System.out.println(getLocalizedText(language, "Grattis! Du skrev det korrekt.", "Congratulations! You typed it correctly."));
            if (wordsPerMinute >= 80) {
                loggedInUser.addPoints(10);
                System.out.println(getLocalizedText(language, "Du får en bonus på 10 poäng för 80 WPM eller mer.", "You get a bonus of 10 points for 80 WPM or more."));
            } else if (wordsPerMinute < 50) {
                loggedInUser.addPoints(-10);
                System.out.println(getLocalizedText(language, "Du förlorar 10 poäng för att ha under 50 WPM.", "You lose 10 points for having under 50 WPM."));
            }
        } else {
            loggedInUser.addPoints(-20);
            System.out.println(getLocalizedText(language, "Felaktigt. Du förlorade 20 poäng.", "Incorrect. You lost 20 points."));
        }

        saveUser(loggedInUser);

        System.out.println(getLocalizedText(language, "Dina poäng: ", "Your points: ") + loggedInUser.getPoints());
        System.out.println(getLocalizedText(language, "Tid tagen: ", "Time taken: ") + totalTimeInSeconds + " seconds.");
        System.out.println(getLocalizedText(language, "Ord per minut: ", "Words per minute: ") + wordsPerMinute);
    }

    private void saveUser(User user) {
        if (user.getId() == null) {
            entityManager.persist(user);
        } else {
            entityManager.merge(user);
        }
    }

    private int calculateWordsPerMinute(int textLength, double totalTimeInSeconds) {
        if (totalTimeInSeconds <= 0) {
            throw new IllegalArgumentException("Total time must be greater than zero.");
        }
        int words = textLength / 5; // Antal ord beräknas baserat på genomsnittlig ordlängd
        // Beräkna antalet ord per minut
        return (int) Math.round((words / totalTimeInSeconds) * 60); // Avrunda resultatet
    }

    private String generateRandomLetters(int count, int level) {
        // Öka antalet bokstäver baserat på nivån
        int adjustedCount = count + (level - 1) * 5; // Lägg till 5 bokstäver per nivå
        StringBuilder randomLetters = new StringBuilder();
        for (int i = 0; i < adjustedCount; i++) {
            randomLetters.append(LETTERS.charAt(random.nextInt(LETTERS.length())));
        }
        return randomLetters.toString();
    }

    private String generateRandomWords(int wordCount, Menu.Language language, int level) {
        // Öka antalet ord baserat på nivån
        int adjustedWordCount = wordCount + (level - 1); // Lägg till 1 ord per nivå
        StringBuilder randomWords = new StringBuilder();
        List<String> words = (language == Menu.Language.SWEDISH) ? SWEDISH_WORDS : ENGLISH_WORDS;

        for (int i = 0; i < adjustedWordCount; i++) {
            String randomWord = words.get(random.nextInt(words.size()));
            randomWords.append(randomWord).append(" ");
        }
        return randomWords.toString().trim();
    }

    private String generateTextWithSpecialCharacters(Menu.Language language, int level) {
        StringBuilder text = new StringBuilder();
        List<String> words = (language == Menu.Language.SWEDISH) ? SWEDISH_WORDS : ENGLISH_WORDS;

        // Öka längden på texten baserat på nivån
        int textLength = random.nextInt(5 + level) + 5; // Minimalt 5 ord, öka med nivån

        for (int i = 0; i < textLength; i++) {
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
        return scanner.nextLine().trim();
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