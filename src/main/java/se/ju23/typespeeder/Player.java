package se.ju23.typespeeder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String playerName;
    private int score;
    private int level;
    private int speed; // Snabbhet
    private int correctCount; // Flest rätt
    private int consecutiveCorrectCount; // Flest rätt i ordning

    public Player(String playerName, int score, int level, int speed, int correctCount, int consecutiveCorrectCount) {
        this.playerName = playerName;
        this.score = score;
        this.level = level;
        this.speed = speed;
        this.correctCount = correctCount;
        this.consecutiveCorrectCount = consecutiveCorrectCount;
    }

    public Player() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public int getConsecutiveCorrectCount() {
        return consecutiveCorrectCount;
    }

    public void setConsecutiveCorrectCount(int consecutiveCorrectCount) {
        this.consecutiveCorrectCount = consecutiveCorrectCount;
    }

}