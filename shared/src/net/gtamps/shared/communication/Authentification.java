package net.gtamps.shared.communication;

public class Authentification implements IRequestData {

    public final String username;
    public final String password;

    public Authentification(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
