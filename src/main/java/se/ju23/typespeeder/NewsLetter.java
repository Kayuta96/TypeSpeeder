package se.ju23.typespeeder;

import java.time.LocalDateTime;

public class NewsLetter {

    private String content;
    private LocalDateTime publishDateTime;

    public NewsLetter() {
    }
    public NewsLetter(String content) {
        if (content == null || content.length() < 100 || content.length() > 200) {
            throw new IllegalArgumentException("Content field length should be between 100 and 200 characters.");
        }
        this.content = content;
        this.publishDateTime = LocalDateTime.now();
    }

    public NewsLetter(String content, NewsLetter newsLetter) {
        this.content = content;
        this.publishDateTime = newsLetter.getPublishDateTime();
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getPublishDateTime() {
        return publishDateTime;
    }

    public void setContent(String content) {
        if (content == null || content.length() < 100 || content.length() > 200) {
            throw new IllegalArgumentException("Content field length should be between 100 and 200 characters.");
        }
        this.content = content;
    }

    public void setPublishDateTime(LocalDateTime publishDateTime) {
        this.publishDateTime = publishDateTime;
    }
}