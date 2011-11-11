package net.gtamps.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * This class is just a wrapper class for now, but later on password encryption
 * could be implemented here.
 *
 * @author jan, tom, til
 */
public class DBPlayer {

    public static boolean hasPlayer(DBHandler db, String username) {
        //usernames and passwords may only contain alpha-numeric characters at the moment.
        Pattern p = Pattern.compile("\\W");
        username = p.matcher(username).replaceAll("");

        ResultSet rs = db.query("SELECT ID FROM PLAYERS WHERE username = '" + username + "'");
        boolean found = false;
        try {
            found = rs.next();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return found;
    }

    public static int authPlayer(DBHandler db, String username, String password) {
        //usernames and passwords may only contain alpha-numeric characters at the moment.
        Pattern p = Pattern.compile("\\W");
        username = p.matcher(username).replaceAll("");
        password = p.matcher(password).replaceAll("");

        ResultSet rs = db.query("SELECT ID FROM PLAYERS WHERE username = '" + username + "' and password ='" + password + "'");
        try {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @param db       the db handler
     * @param username the desired username
     * @param password the orresponding password
     * @return the new player uid if successful or -1 on error (for example duplicate username);
     */
    public static int createPlayer(DBHandler db, String username, String password) {
        Pattern p = Pattern.compile("\\W");
        username = p.matcher(username).replaceAll("");
        password = p.matcher(password).replaceAll("");
        ResultSet rs;
        rs = db.query("SELECT ID FROM PLAYERS WHERE username = '" + username + "'");
        try {
            if (rs.next()) {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        rs = db.query("INSERT INTO PLAYERS (username, password) values ('" + username + "','" + password + "')");
        rs = db.query("SELECT ID FROM PLAYERS WHERE username = '" + username + "'");
        try {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void deletePlayer(DBHandler db, String username, String password) {
        Pattern p = Pattern.compile("\\W");
        username = p.matcher(username).replaceAll("");
        password = p.matcher(password).replaceAll("");
        db.query("DELETE FROM PLAYERS WHERE username = '" + username + "' and password ='" + password + "'");
    }
}
