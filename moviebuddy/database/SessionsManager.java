package com.moviebuddy.database;

import org.telegram.telegrambots.logging.BotLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Greg
 * @version 1.0
 * @brief Handler for updates
 * @date 08/14/16
 */

public class SessionsManager {

    private static final String LOGTAG = "DATABASEMANAGER";

    private static volatile SessionsConnect connetion;
    private static volatile SessionsManager instance;

    /**
     * Private constructor (due to Singleton)
     */
    private SessionsManager(){
        connetion = new SessionsConnect();
        BotLogger.info(LOGTAG,"Connection Successful");
    }

    /**
     * Get Singleton instance
     *
     * @return instance of the class
     */
    public static SessionsManager getInstance() {
        final SessionsManager currentInstance;
        if (instance == null) {
            synchronized (SessionsManager.class) {
                if (instance == null) {
                    instance = new SessionsManager();
                }
                currentInstance = instance;
            }
        } else {
            currentInstance = instance;
        }
        return currentInstance;
    }

    public Integer get(Integer userId){
        Integer state = 0;

        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement("SELECT idCountry FROM Movie.Movies WHERE idMovie = ?");
            preparedStatement.setInt(1, 1);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                state = result.getInt("idCountry");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return state;
    }


}
