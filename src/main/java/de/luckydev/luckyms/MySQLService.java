package de.luckydev.luckyms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLService {
    public String host;
    public int port;
    public String username;
    public String password;
    public Connection connection;

    public MySQLService(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public MySQLDatabase connectToDB(String name) throws MySQLException {
        MySQLDatabase database = new MySQLDatabase(name, this);
        database.connect();
        return database;
    }

    public MySQLService createDB(String name) throws  MySQLException {
        try {
            connection.createStatement().execute("CREATE DATABASE `" + name + "`");
        } catch (SQLException exception) {
            throw new MySQLException("Exception occurred while creating database '" + name + "'!\nSQLException: " + exception.toString());
        }
        return this;
    }

    public MySQLService createDBIfNotExists(String name) throws  MySQLException {
        try {
            connection.createStatement().execute("CREATE DATABASE IF NOT EXISTS `" + name + "`");
        } catch (SQLException exception) {
            throw new MySQLException("Exception occurred while creating database '" + name + "'!\nSQLException: " + exception.toString());
        }
        return this;
    }

    public MySQLService deleteDB(String name) throws  MySQLException {
        try {
            connection.createStatement().execute("DROP DATABASE  `" + name + "`");
        } catch(SQLException exception) {
            throw new MySQLException("Exception occurred while deleting database '" + name + "'!\nSQLException: " + exception.toString());
        }
        return this;
    }

    public MySQLService connect() throws MySQLException {
        if(isConnected()) return this;
        try {
            connection = DriverManager.getConnection(getURL(), username, password);
            return this;
        } catch (SQLException exception) {
            throw new MySQLException("Exception occurred while connecting with " + getURL() + "!\nSQLException: " + exception.toString());
        }
    }

    public MySQLService disconnect() throws MySQLException {
        if(!isConnected()) return this;
        try {
            connection.close();
            return this;
        } catch (SQLException exception) {
            throw new MySQLException("Exception occurred while disconnecting with " + getURL() + "!\nSQLException: " + exception.toString());
        }
    }

    public boolean isConnected() {
        return connection != null;
    }

    public String getURL() {
        return "jdbc:mysql://" + host + ":" + port + "/";
    }
}
