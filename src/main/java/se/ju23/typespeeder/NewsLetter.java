package se.ju23.typespeeder;

import java.time.LocalDateTime;

public class NewsLetter {

    private String content;
    private LocalDateTime publishDateTime;

    public NewsLetter() {
        this.content = ""; // Tomt innehåll som måste uppdateras senare
        this.publishDateTime = LocalDateTime.now(); // Sätt publiceringstiden till nuvarande tid
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getPublishDateTime() {
        return publishDateTime;
    }
}