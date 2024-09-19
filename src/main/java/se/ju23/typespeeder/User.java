package se.ju23.typespeeder;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    private int points;
    private int level;
    @Column(name = "player_name")
    private String playerName;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserStatistics userStatistics;

    public User() {
        this.userStatistics = new UserStatistics();
        this.userStatistics.setUser(this);
        this.points = 0;
        this.level = 1; // Starta på nivå 1
    }

    public User(String username, String password, String playerName) {
        this();
        this.username = username;
        this.password = password;
        this.playerName = playerName;
    }

    public void addPoints(int points) {
        this.points += points;

        // Kolla om användaren når 100 poäng för att levela upp
        if (this.points >= 100) {
            levelUp();
        }
    }

    private void levelUp() {
        this.level++;
        this.points = 0; // Återställ poängen till 0 efter nivåuppgång
        System.out.println("Grattis! Du har nått nivå " + level + "!"); // Meddelande vid nivåuppgång
    }

    public int getPoints() {
        return points;
    }

    public int getLevel() {
        return level;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public UserStatistics getUserStatistics() {
        return userStatistics;
    }

    public void setUserStatistics(UserStatistics userStatistics) {
        this.userStatistics = userStatistics;
        userStatistics.setUser(this);
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", playerName='" + playerName + '\'' +
                ", points=" + points +
                ", level=" + level +
                '}';
    }
}