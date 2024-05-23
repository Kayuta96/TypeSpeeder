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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public UserStatistics() {
    }

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

    public void updateStats(double speed, int correct, int inOrderCorrect) {
        this.averageSpeed = (this.averageSpeed * (this.totalCorrect + correct) + speed) / (this.totalCorrect + correct);
        this.totalCorrect += correct;
        this.totalInOrderCorrect += inOrderCorrect;
    }
}