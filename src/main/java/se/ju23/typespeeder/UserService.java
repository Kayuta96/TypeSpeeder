package se.ju23.typespeeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserStatisticsRepository userStatisticsRepository;

    @Autowired
    public UserService(UserStatisticsRepository userStatisticsRepository) {
        this.userStatisticsRepository = userStatisticsRepository;
    }

    public void updateUserStatistics(User user, int wordsPerMinute) {
        UserStatistics userStatistics = userStatisticsRepository.findByUser(user);
        if (userStatistics == null) {
            userStatistics = new UserStatistics();
            userStatistics.setUser(user);
            userStatistics.setAverageSpeed(wordsPerMinute);
            userStatistics.setCorrectCount(1);
            userStatistics.setTotalCorrect(1);
            userStatistics.setTotalInOrderCorrect(1);
            userStatistics.setConsecutiveCorrectCount(1);
        } else {
            userStatistics.updateStats(wordsPerMinute, 1, 1);
        }

        userStatisticsRepository.save(userStatistics);
    }
}