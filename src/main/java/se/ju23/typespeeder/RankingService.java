package se.ju23.typespeeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RankingService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserStatisticsRepository userStatisticsRepository;

    public List<User> getRankedUsers() {
        List<User> users = userRepository.findAll();

        users.sort(Comparator.comparingDouble(this::calculateCompositeScore).reversed());

        return users;
    }

    public List<UserStatistics> getRankedUsersByWPM() {
        // Hämta alla UserStatistics och sortera efter averageSpeed
        return userStatisticsRepository.findAllByOrderByAverageSpeedDesc();
    }

    public double calculateCompositeScore(User user) {
        double speedScore = 1 / user.getUserStatistics().getAverageSpeed(); // Lägre tid är bättre
        double correctScore = user.getUserStatistics().getTotalCorrect();
        double inOrderCorrectScore = user.getUserStatistics().getTotalInOrderCorrect();

        // Samlad bedömning
        return 0.5 * speedScore + 0.3 * correctScore + 0.2 * inOrderCorrectScore;
    }
}
