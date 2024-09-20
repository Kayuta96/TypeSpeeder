package se.ju23.typespeeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {
    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostMapping("/admin/create")
    public String createNewsUpdate(@RequestParam String title, @RequestParam String content) {
        newsService.createNewsUpdate(title, content);
        return "Nyhetsuppdatering skapad!";
    }

    @GetMapping("/all")
    public List<NewsUpdate> getAllNewsUpdates() {
        return newsService.getAllNewsUpdates();
    }
}