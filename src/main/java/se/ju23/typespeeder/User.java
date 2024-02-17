package se.ju23.typespeeder;

public class User {
    private String username;
    private String password;
    private String playerName;

    //Konstruktor
    public User(String username, String passworrd, String playerName) {
        this.username = username;
        this.password = password;
        this.playerName = playerName
    }
    // Getter och setter för användarnamn
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter och setter för lösenord
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    // Getter och setter för spelarnamn
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

}
