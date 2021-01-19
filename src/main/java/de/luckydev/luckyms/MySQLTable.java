package de.luckydev.luckyms;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class MySQLTable {
    public MySQLDatabase database;
    public MySQLService service;
    public String name;

    public MySQLTable(MySQLDatabase database, MySQLService service, String name) {
        this.database = database;
        this.service = service;
        this.name = name;
    }

    public String sGetWhereIs(String column, String where, String is) throws MySQLException {
        try {
            ResultSet resultSet = database.connection.createStatement().executeQuery("SELECT * FROM `" + name + "` WHERE `" + where + "`='" + is + "'");
            resultSet.next();
            return resultSet.getString(column);
        } catch(SQLException exception) {
            throw new MySQLException("Exception occurred while selecting stuff in table '" + name + "'!\nSQLException: " + exception.toString());
        }
    }

    public Integer iGetWhereIs(String column, String where, String is) throws MySQLException {
        try {
            ResultSet resultSet = database.connection.createStatement().executeQuery("SELECT * FROM `" + name + "` WHERE `" + where + "`='" + is + "'");
            resultSet.next();
            return resultSet.getInt(column);
        } catch(SQLException exception) {
            throw new MySQLException("Exception occurred while selecting stuff in table '" + name + "'!\nSQLException: " + exception.toString());
        }
    }

    public Boolean bGetWhereIs(String column, String where, String is) throws MySQLException {
        try {
            ResultSet resultSet = database.connection.createStatement().executeQuery("SELECT * FROM `" + name + "` WHERE `" + where + "`='" + is + "'");
            resultSet.next();
            return resultSet.getBoolean(column);
        } catch(SQLException exception) {
            throw new MySQLException("Exception occurred while selecting stuff in table '" + name + "'!\nSQLException: " + exception.toString());
        }
    }

    public Long lGetWhereIs(String column, String where, String is) throws MySQLException {
        try {
            ResultSet resultSet = database.connection.createStatement().executeQuery("SELECT * FROM `" + name + "` WHERE `" + where + "`='" + is + "'");
            resultSet.next();
            return resultSet.getLong(column);
        } catch(SQLException exception) {
            throw new MySQLException("Exception occurred while selecting stuff in table '" + name + "'!\nSQLException: " + exception.toString());
        }
    }

    public ArrayList<Long> lGetAllWhereIs(String column, String where, String is) throws MySQLException {
        try {
            ArrayList<Long> out = new ArrayList<>();
            ResultSet resultSet = database.connection.createStatement().executeQuery("SELECT * FROM `" + name + "` WHERE `" + where + "` = '" + is + "'");
            while(resultSet.next())
                out.add(resultSet.getLong(column));
            return out;
        } catch(SQLException exception) {
            throw new MySQLException("Exception occurred while selecting stuff in table '" + name + "'!\nSQLException: " + exception.toString());
        }
    }

    public ArrayList<String> sGetAllWhereIs(String column, String where, String is) throws MySQLException {
        try {
            ArrayList<String> out = new ArrayList<>();
            ResultSet resultSet = database.connection.createStatement().executeQuery("SELECT * FROM `" + name + "` WHERE `" + where + "` = '" + is + "'");
            while(resultSet.next())
                out.add(resultSet.getString(column));
            return out;
        } catch(SQLException exception) {
            throw new MySQLException("Exception occurred while selecting stuff in table '" + name + "'!\nSQLException: " + exception.toString());
        }
    }

    public ArrayList<String> getContents(String column) throws MySQLException {
        try {
            ArrayList<String> out = new ArrayList<>();
            ResultSet resultSet = database.connection.createStatement().executeQuery("SELECT * FROM `" + name + "`");
            while(resultSet.next())
                out.add(resultSet.getString(column));
            return out;
        } catch(SQLException exception) {
            throw new MySQLException("Exception occurred while selecting stuff in table '" + name + "'!\nSQLException: " + exception.toString());
        }
    }

    public MySQLTable insert(String... values) throws MySQLException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO `").append(name).append("` VALUES (");
            boolean next = false;
            for(String value : values) { sql.append(next?", '":"'").append(value).append("'"); next = true; }
            sql.append(")");
            database.connection.createStatement().execute(sql.toString());
            return this;
        } catch(SQLException exception) {
            throw new MySQLException("Exception occurred while inserting stuff in table '" + name + "'!\nSQLException: " + exception.toString());
        }
    }

    public MySQLTable deleteAllWhereIs(String where, String is) throws MySQLException {
        try {
            database.connection.createStatement().execute("DELETE FROM `" + name + "` WHERE `" + name + "`.`" + where + "` = '" + is + "'");
        } catch (SQLException exception) {
            throw new MySQLException("Exception occurred while deleting stuff in table '" + name + "'!\nSQLException: " + exception.toString());
        }
        return this;
    }

    public MySQLTable updateWhereIs(String update, String value, String where, String is) throws MySQLException {
        try {
            database.connection.createStatement().execute("UPDATE `" + name + "` SET `" + update + "` = '" + value + "' WHERE `" + name + "`.`" + where + "` = '" + is + "'");
        } catch (SQLException exception) {
            throw new MySQLException("Exception occurred while updating stuff in table '" + name + "'!\nSQLException: " + exception.toString());
        }
        return  this;
    }

    public boolean exists(String key, String value) {
        try {
            return database.connection.createStatement().executeQuery("SELECT * FROM `" + name + "` WHERE `" + name + "`.`" + key + "`='" + value + "'").next();
        } catch(SQLException exception) {
            return false;
        }
    }
}
