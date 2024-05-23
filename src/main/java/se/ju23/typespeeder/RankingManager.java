package se.ju23.typespeeder;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RankingManager {

    public static void showRankings(List<User> users) {
        List<User> rankedUsers = rankUsers(users);

        System.out.println("Rankings List:");
        System.out.println("--------------");
        for (int i = 0; i < rankedUsers.size(); i++) {
            User user = rankedUsers.get(i);
            System.out.println((i + 1) + ". " + user.getUsername() + " - Score: " + calculateScore(user));
        }
    }

    private static List<User> rankUsers(List<User> users) {
        return users.stream()
                .sorted(Comparator.comparingInt(RankingManager::calculateScore).reversed())
                .collect(Collectors.toList());
    }

    private static int calculateScore(User user) {
        UserStatistics userStatistics = user.getUserStatistics();
        if (userStatistics == null) {
            return 0;
        }

        // Hämta relevanta statistikvariabler
        double speed = userStatistics.getSpeed();
        int correctCount = userStatistics.getCorrectCount();
        int consecutiveCorrectCount = userStatistics.getConsecutiveCorrectCount();

        return (int) ((speed * 2) + (correctCount * 3) + (consecutiveCorrectCount * 5));
    }
}