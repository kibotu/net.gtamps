package net.gtamps.shared.communication;

public class AuthentificationData implements ISendableData {

    public final String username;
    public final String password;

    public AuthentificationData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Authentification{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
