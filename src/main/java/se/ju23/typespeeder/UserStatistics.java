package se.ju23.typespeeder;

import jakarta.persistence.*;

@Entity
@Table(name = "user_statistics")
public class UserStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "speed")
    private Double speed;

    @Column(name = "correct_count")
    private Integer correctCount;

    @Column(name = "consecutive_correct_count")
    private Integer consecutiveCorrectCount;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(Integer correctCount) {
        this.correctCount = correctCount;
    }

    public Integer getConsecutiveCorrectCount() {
        return consecutiveCorrectCount;
    }

    public void setConsecutiveCorrectCount(Integer consecutiveCorrectCount) {
        this.consecutiveCorrectCount = consecutiveCorrectCount;
    }
    public void updateSpeed(int wordsPerMinute, double totalTimeInSeconds) {
        double newSpeed = calculateSpeed(wordsPerMinute, totalTimeInSeconds);
        setSpeed(newSpeed);
    }

    private double calculateSpeed(int wordsPerMinute, double totalTimeInSeconds) {
        return wordsPerMinute / totalTimeInSeconds * 60;
    }

}