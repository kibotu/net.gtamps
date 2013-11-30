package net.gtamps.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.gtamps.server.ServerException;
import net.gtamps.server.gui.GUILogger;
import net.gtamps.server.gui.LogType;

public class DBHandler {
    LogType TAG = LogType.DB;
    Connection conn;

    public DBHandler(final String path) throws ServerException {
        final JDBCConnector jdbc = new JDBCConnector(path);
        conn = jdbc.getConnection();
        if (conn == null) {
            GUILogger.i().log(TAG, "DATABASE CONNECTION FAILED");
        } else {
            GUILogger.i().log(TAG, "DATABASE CONNECTION SUCCESSFUL");
        }
    }

    protected ResultSet query(final String sqlquery) {
        try {
            final Statement stat = conn.createStatement();
            stat.execute(sqlquery);
            return stat.getResultSet();
        } catch (final SQLException e) {
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
    public boolean hasPlayer(final String username) {
        final boolean found = DBPlayer.hasPlayer(this, username);
        if (found) {
            GUILogger.i().log(TAG, "player lookup [" + username + "] returned true!");
        } else {
            GUILogger.i().log(TAG, "player lookup [" + username + "] returned false!");
        }
        return found;
    }

    /**
     * if a player is successfully authenticated this method returns the player id inside the Database,
     * otherwise it returns -1
     *
     * @return a valid player id (&gt;1) or -1 as error-id
     */
    public int authPlayer(final String username, final String password) {
        final int uid = DBPlayer.authPlayer(this, username, password);
        if (uid != -1) {
            GUILogger.i().log(TAG, "AUTH player [" + username + ":" + uid + "] successful!");
        } else {
            GUILogger.i().log(TAG, "AUTH player [" + username + "] failed!");
        }
        return uid;
    }

    public int createPlayer(final String username, final String password) {
        final int uid = DBPlayer.createPlayer(this, username, password);
        if (uid != -1) {
            GUILogger.i().log(TAG, "Creation of player [" + username + ":" + uid + "] successful!");
        } else {
            GUILogger.i().log(TAG, "Creation of player [" + username + "] failed! (try different username)");
        }
        return uid;
    }

    public void deletePlayer(final String username, final String password) {
        GUILogger.i().log(TAG, "Deleting player [" + username + "] if existing...");
        DBPlayer.deletePlayer(this, username, password);
    }

}
