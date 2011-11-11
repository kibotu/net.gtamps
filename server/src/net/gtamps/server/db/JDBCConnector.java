package net.gtamps.server.db;

import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class JDBCConnector {
    private LogType TAG = LogType.DB;
    private String path;

    public JDBCConnector(String path) {
        this.path = path;
        try {
            Driver driverClass = (Driver) DriverManager.getDriver("jdbc:derby:" + path);
            Logger.i().log(TAG, "Driver for jdbc:derby:" + path);
            Logger.i().log(TAG, "   " + driverClass.getClass().getName());
            listDrivers();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void listDrivers() {
        Enumeration driverList = DriverManager.getDrivers();
        Logger.i().log(TAG, "List of drivers:");
        while (driverList.hasMoreElements()) {
            Driver driverClass = (Driver) driverList.nextElement();
            Logger.i().log(TAG, "   " + driverClass.getClass().getName());
        }
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:derby:" + path);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
