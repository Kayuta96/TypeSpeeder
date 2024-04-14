package se.ju23.typespeeder;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class WordRepository {
    private static final List<String> WORDS = List.of(
            "apple", "banana","star", "chocolate", "programming", "challenge", "keyboard", "algorithm", "java", "spring", "developer"
            // Lägg till fler ord här
    );

    private static final Random random = new Random();

    public static String getRandomWord() {
        return WORDS.get(random.nextInt(WORDS.size()));
    }

    public List<String> getRandomWords(int count) {
        List<String> randomWords = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            randomWords.add(getRandomWord());
        }
        return randomWords;
    }
}