package com.moviebuddy.database;

import org.telegram.telegrambots.logging.BotLogger;

import java.sql.*;

/**
 * @author Greg
 * @version 1.0
 * @brief Handler for updates
 * @date 08/14/16
 */

public class SessionsConnect {

    private static final String LOGTAG = "SESSIONS_DATABASE_CONNECTION";
    private java.sql.Connection currentConection;

    private static final String linkDB = "jdbc:mysql://127.0.0.1:3306/Session";//?useUnicode=true&characterEncoding=UTF-8
    private static final String controllerDB = "com.mysql.jdbc.Driver";
    private static final String userDB = "root";
    private static final String password = "matiss";

    public SessionsConnect() {
        this.currentConection = openConexion();
    }

    private Connection openConexion() {
        Connection connection = null;
        try {
            Class.forName(controllerDB).newInstance();
            connection = DriverManager.getConnection(linkDB, userDB, password);
        } catch (SQLException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            BotLogger.error(LOGTAG, e);
        }

        return connection;
    }

    public void closeConexion() {
        try {
            this.currentConection.close();
        } catch (SQLException e) {
            BotLogger.error(LOGTAG, e);
        }

    }

    public ResultSet runSqlQuery(String query) throws SQLException {
        final Statement statement;
        statement = this.currentConection.createStatement();
        return statement.executeQuery(query);
    }

    public Boolean executeQuery(String query) throws SQLException {
        final Statement statement = this.currentConection.createStatement();
        return statement.execute(query);
    }

    public PreparedStatement getPreparedStatement(String query) throws SQLException {
        return this.currentConection.prepareStatement(query);
    }

    /**
     * Initilize a transaction in com.moviebuddy.database
     * @throws SQLException If initialization fails
     */
    public void initTransaction() throws SQLException {
        this.currentConection.setAutoCommit(false);
    }

    /**
     * Finish a transaction in com.moviebuddy.database and commit changes
     * @throws SQLException If a rollback fails
     */
    public void commitTransaction() throws SQLException {
        try {
            this.currentConection.commit();
        } catch (SQLException e) {
            if (this.currentConection != null) {
                this.currentConection.rollback();
            }
        } finally {
            this.currentConection.setAutoCommit(false);
        }
    }
}
