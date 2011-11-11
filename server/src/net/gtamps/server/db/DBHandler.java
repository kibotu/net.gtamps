package net.gtamps.server.db;

import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHandler {
    LogType TAG = LogType.DB;
    Connection conn;

    public DBHandler(String path) {
        JDBCConnector jdbc = new JDBCConnector(path);
        conn = jdbc.getConnection();
        if (conn == null) {
            Logger.i().log(TAG, "DATABASE CONNECTION FAILED");
        } else {
            Logger.i().log(TAG, "DATABASE CONNECTION SUCCESSFUL");
        }
    }

    protected ResultSet query(String sqlquery) {
        try {
            Statement stat = conn.createStatement();
            stat.execute(sqlquery);
            return stat.getResultSet();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * check if a player with <code>username</code> already exists in the
     * database.
     *
     * @param username
     * @return
     */
    public boolean hasPlayer(String username) {
        boolean found = DBPlayer.hasPlayer(this, username);
        if (found) {
            Logger.i().log(TAG, "player lookup [" + username + "] returned true!");
        } else {
            Logger.i().log(TAG, "player lookup [" + username + "] returned false!");
        }
        return found;
    }

    /**
     * if a player is successfully authenticated this method returns the player id inside the Database,
     * otherwise it returns -1
     *
     * @return a valid player id (&gt;1) or -1 as error-id
     */
    public int authPlayer(String username, String password) {
        int uid = DBPlayer.authPlayer(this, username, password);
        if (uid != -1) {
            Logger.i().log(TAG, "AUTH player [" + username + ":" + uid + "] successful!");
        } else {
            Logger.i().log(TAG, "AUTH player [" + username + "] failed!");
        }
        return uid;
    }

    public int createPlayer(String username, String password) {
        int uid = DBPlayer.createPlayer(this, username, password);
        if (uid != -1) {
            Logger.i().log(TAG, "Creation of player [" + username + ":" + uid + "] successful!");
        } else {
            Logger.i().log(TAG, "Creation of player [" + username + "] failed! (try different username)");
        }
        return uid;
    }

    public void deletePlayer(String username, String password) {
        Logger.i().log(TAG, "Deleting player [" + username + "] if existing...");
        DBPlayer.deletePlayer(this, username, password);
    }

}
