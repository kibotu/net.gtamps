package net.gtamps.shared.serializer.communication.data;

import net.gtamps.shared.SharedObject;

public class AuthentificationData extends SharedObject implements ISendableData {

    private static final long serialVersionUID = -2582506868524601136L;
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
