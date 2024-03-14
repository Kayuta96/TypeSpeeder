package se.ju23.typespeeder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NewsLetter {
    private String content;
    private LocalDateTime publishDateTime;

    public NewsLetter(String content, LocalDateTime publishDateTime) {
        this.content = content;
        this.publishDateTime = publishDateTime;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getPublishDateTime() {
        return publishDateTime;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = publishDateTime.format(formatter);
        return "Content: " + content + "\nPublished: " + formattedDateTime;
    }
}