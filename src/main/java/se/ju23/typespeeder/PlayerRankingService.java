package se.ju23.typespeeder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerRankingService {

    @PersistenceContext
    private EntityManager entityManager;

    public PlayerRankingService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void addUser(User user) {
        entityManager.persist(user);
    }

    public List<UserStatistics> getTopUsersByChallengeType(ChallengeType challengeType) {
        switch (challengeType) {
            case SPEED:
                return entityManager.createQuery("SELECT us FROM UserStatistics us WHERE us.speed IS NOT NULL ORDER BY us.speed DESC", UserStatistics.class)
                        .setMaxResults(5) // Hämta topp 5
                        .getResultList();
            case CORRECT_COUNT:
                return entityManager.createQuery("SELECT us FROM UserStatistics us WHERE us.correctCount IS NOT NULL ORDER BY us.correctCount DESC", UserStatistics.class)
                        .setMaxResults(5) // Hämta topp 5
                        .getResultList();
            case CONSECUTIVE_CORRECT_COUNT:
                return entityManager.createQuery("SELECT us FROM UserStatistics us WHERE us.consecutiveCorrectCount IS NOT NULL ORDER BY us.consecutiveCorrectCount DESC", UserStatistics.class)
                        .setMaxResults(5) // Hämta topp 5
                        .getResultList();
            case COMBINED_SCORE:
                return entityManager.createQuery("SELECT us FROM UserStatistics us WHERE us.speed IS NOT NULL AND us.correctCount IS NOT NULL AND us.consecutiveCorrectCount IS NOT NULL ORDER BY (us.speed + us.correctCount + us.consecutiveCorrectCount) DESC", UserStatistics.class)
                        .setMaxResults(5) // Hämta topp 5
                        .getResultList();
            default:
                throw new IllegalArgumentException("Invalid challenge type");
        }
    }

    public List<UserStatistics> getTopUsersByOverallScore() {
        List<UserStatistics> topUsersBySpeed = getTopUsersByChallengeType(ChallengeType.SPEED);
        List<UserStatistics> topUsersByCorrectCount = getTopUsersByChallengeType(ChallengeType.CORRECT_COUNT);
        List<UserStatistics> topUsersByConsecutiveCorrectCount = getTopUsersByChallengeType(ChallengeType.CONSECUTIVE_CORRECT_COUNT);

        Map<UserStatistics, Integer> overallScores = new HashMap<>();

        for (UserStatistics userStats : topUsersBySpeed) {
            overallScores.put(userStats, 0);
        }

        for (UserStatistics userStats : topUsersByCorrectCount) {
            overallScores.putIfAbsent(userStats, 0);
            overallScores.put(userStats, overallScores.get(userStats) + 1); // Används för att räkna upp för varje användare som dyker upp i topUsersByCorrectCount
        }

        for (UserStatistics userStats : topUsersByConsecutiveCorrectCount) {
            overallScores.putIfAbsent(userStats, 0);
            overallScores.put(userStats, overallScores.get(userStats) + 1); // Används för att räkna upp för varje användare som dyker upp i topUsersByConsecutiveCorrectCount
        }

        List<UserStatistics> topUsersByOverallScore = overallScores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return topUsersByOverallScore.subList(0, Math.min(5, topUsersByOverallScore.size())); // Returnera de fem bästa användarna eller färre om det finns färre än fem
    }

    public enum ChallengeType {
        SPEED,
        CORRECT_COUNT,
        CONSECUTIVE_CORRECT_COUNT,
        COMBINED_SCORE
    }
}