package net.gtamps.server.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import net.gtamps.server.ServerException;
import net.gtamps.server.gui.GUILogger;
import net.gtamps.server.gui.LogType;

public class JDBCConnector {
    private final LogType TAG = LogType.DB;
    private final String path;

    public JDBCConnector(final String path) {
        this.path = path;
        try {
            final Driver driverClass = DriverManager.getDriver("jdbc:derby:" + path);
            GUILogger.i().log(TAG, "Driver for jdbc:derby:" + path);
            GUILogger.i().log(TAG, "   " + driverClass.getClass().getName());
            listDrivers();

        } catch (final SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void listDrivers() {
        final Enumeration driverList = DriverManager.getDrivers();
        GUILogger.i().log(TAG, "List of drivers:");
        while (driverList.hasMoreElements()) {
            final Driver driverClass = (Driver) driverList.nextElement();
            GUILogger.i().log(TAG, "   " + driverClass.getClass().getName());
        }
    }

    public Connection getConnection() throws ServerException {
        try {
            return DriverManager.getConnection("jdbc:derby:" + path);
        } catch (final SQLException e) {
        	e.printStackTrace();
            throw new ServerException("could not connect to database", e);
        }
    }
}
