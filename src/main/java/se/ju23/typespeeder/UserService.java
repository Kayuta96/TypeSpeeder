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

    // Uppdaterar användarens statistik, och endast om WPM är högre än nuvarande bästa
    public void updateUserStatistics(User user, int wordsPerMinute, Menu.Language language) {
        try {
            UserStatistics userStatistics = userStatisticsRepository.findByUser(user);

            if (userStatistics == null) {
                // Skapa ny statistik om den inte finns
                userStatistics = new UserStatistics();
                userStatistics.setUser(user);
                userStatistics.setBestWpm(wordsPerMinute);  // Sätt initial WPM som bästa WPM
                System.out.println(getLocalizedText(language,
                        "Ny statistik skapad för användare: ", "New statistics created for user: ") + user.getUsername());
            } else if (wordsPerMinute > userStatistics.getBestWpm()) {
                // Uppdatera endast om nuvarande WPM är högre än bästa WPM
                userStatistics.setBestWpm(wordsPerMinute);
                System.out.println(getLocalizedText(language,
                        "Ny bästa WPM: ", "New best WPM: ") + wordsPerMinute + " " +
                        getLocalizedText(language, "för användare: ", "for user: ") + user.getUsername());
            } else {
                System.out.println(getLocalizedText(language,
                        "WPM är lägre än nuvarande bästa WPM, ingen uppdatering gjord.",
                        "WPM is lower than current best WPM, no update made."));
            }

            // Spara uppdaterad statistik
            userStatisticsRepository.save(userStatistics);

        } catch (Exception e) {
            System.out.println(getLocalizedText(language,
                    "Fel vid uppdatering av användarstatistik: ",
                    "Error updating user statistics: ") + e.getMessage());
            e.printStackTrace();
        }
    }

    // Hanterar text baserat på vald språk
    private String getLocalizedText(Menu.Language language, String swedish, String english) {
        return language == Menu.Language.SWEDISH ? swedish : english;
    }
}