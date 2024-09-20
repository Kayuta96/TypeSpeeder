package se.ju23.typespeeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {
    private final NewsUpdateRepository newsUpdateRepository;

    @Autowired
    public NewsService(NewsUpdateRepository newsUpdateRepository) {
        this.newsUpdateRepository = newsUpdateRepository;
    }

    // Skapa nyhetsuppdatering
    public void createNewsUpdate(String title, String content) {
        NewsUpdate newsUpdate = new NewsUpdate(title, content);
        newsUpdateRepository.save(newsUpdate);
    }

    // Hämta alla nyheter i fallande ordning
    public List<NewsUpdate> getAllNewsUpdates() {
        return newsUpdateRepository.findAllByOrderByTimestampDesc();
    }
}