package se.ju23.typespeeder;

import jakarta.persistence.*;

@Entity
public class UserStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double averageSpeed;
    private int totalCorrect;
    private int totalInOrderCorrect;
    private int correctCount;
    private double speed;

    @Column(name = "bestwpm")
    private int bestWpm;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public UserStatistics() {
    }

    @Column(name = "consecutive_correct_count")
    private int consecutiveCorrectCount;

    public int getBestWpm() {
        return bestWpm;
    }

    public void setBestWpm(int bestWpm) {
        this.bestWpm = bestWpm;
    }

    // Other getters and setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public int getTotalCorrect() {
        return totalCorrect;
    }

    public void setTotalCorrect(int totalCorrect) {
        this.totalCorrect = totalCorrect;
    }

    public int getTotalInOrderCorrect() {
        return totalInOrderCorrect;
    }

    public void setTotalInOrderCorrect(int totalInOrderCorrect) {
        this.totalInOrderCorrect = totalInOrderCorrect;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
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


    public void updateStats(double speed, int correct, int inOrderCorrect) {
        this.averageSpeed = (this.averageSpeed * (this.totalCorrect + correct) + speed) / (this.totalCorrect + correct);
        this.totalCorrect += correct;
        this.totalInOrderCorrect += inOrderCorrect;


        if (speed > this.bestWpm) {
            this.bestWpm = (int) speed;
        }
    }
}