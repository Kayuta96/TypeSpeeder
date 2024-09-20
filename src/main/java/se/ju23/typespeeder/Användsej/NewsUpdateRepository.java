package se.ju23.typespeeder;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsUpdateRepository extends JpaRepository<NewsUpdate, Long> {
    // Hämta nyheter i fallande ordning efter skapningsdatum
    List<NewsUpdate> findAllByOrderByTimestampDesc();
}