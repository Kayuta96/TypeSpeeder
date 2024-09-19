package se.ju23.typespeeder;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Long> {
    List<UserStatistics> findAllByOrderByAverageSpeedDesc();
}