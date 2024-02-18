package se.ju23.typespeeder;
import java.util.ArrayList;
import java.util.List;

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

    //Metod för att se och verifiera inloggninsuppgifter
    public boolean authenticator(String enteredUsername, String enteredPassword) {
        return username.equals(enteredUsername) && password.equals(enteredPassword);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", playerName='" + playerName + '\'' +
                '}';
    }

    public static void main(String[] args) {
        List<User> userList = new Arraylist<>();
        userList.add(new User("Admin", "password", "Conny"));

        //Inloggning
        String enteredUsername = "Admin";
        String enteredPassword = "password";

        for (User user : userList) {
            if (user.authenticator(enteredUsername, enteredPassword)) {
                System.out.println("Inloggning lyckades för användare: " + user.getPlayerName());
                break;
            }
        }
    }
}