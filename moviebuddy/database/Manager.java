package com.moviebuddy.database;

import com.moviebuddy.BotHandlers;
import org.telegram.telegrambots.logging.BotLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static java.lang.Thread.sleep;


/**
 * @author Greg
 * @version 1.0
 * @brief Data Manager (local)
 * @date 07/14/16
 */

public class Manager {

    private static final String LOGTAG = "DATABASEMANAGER";

    private static volatile DBconnection connetion;
    private static volatile Manager instance;

    /**
     * Private constructor (due to Singleton)
     */
    private Manager(){
        connetion = new DBconnection();
        BotLogger.info(LOGTAG,"Connection Successful");
    }

    /**
     * Get Singleton instance
     *
     * @return instance of the class
     */
    public static Manager getInstance() {
        final Manager currentInstance;
        if (instance == null) {
            synchronized (Manager.class) {
                if (instance == null) {
                    instance = new Manager();
                }
                currentInstance = instance;
            }
        } else {
            currentInstance = instance;
        }
        return currentInstance;
    }

    public static boolean checkCurrentTime(String time){//String date

        Date daydate = new Date();
        String day = new SimpleDateFormat("dd-MM-yy").format(daydate);

        String timeProper = time +  " " + day ;

        DateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yy");
        Date date2 = null;
        try {
            date2 = format.parse(timeProper);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date current = new Date();

        return (date2.compareTo(current)==1)?(true):(false);
    }

    public static HashMap<Integer,String[]> getSessions(String movieName, int userId){

        /**
         * Get users distance and price preference from users database
         * @returns strDistDraft, strPriceDraft
         */

        String strDistDraft = new String();
        String strPriceDraft = new String();
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement("SELECT dist,price FROM users WHERE user_id = ?");
            preparedStatement.setInt(1, userId);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                strDistDraft = result.getString("dist");
                strPriceDraft = result.getString("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /**
         * Get cinemas and their addresses
         * @returns HashMap<String,String> hashCinemas
         */

        String cinemaName ="";
        String cinemaAddress ="";
        HashMap<String,String> hashCinemas = new HashMap<>();
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement("select name,address from Cinema.Cinemas");
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                cinemaName = result.getString("name");
                cinemaAddress = result.getString("address");
                hashCinemas.put(cinemaName,cinemaAddress);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /**
         * Searches sessions by distance and price
         *
         * Finds closest cinema
         * @returns cinemaNameMin
         * Find cinemas in range
         * @returns cinemasList
         */

        HashMap<Integer,String[]> hashSessions = new HashMap<>();

        /**
         * Default query
         */
        String defaultQuery = "select Session.Sessions.price as Price,Session.Sessions.time as Time,form.name as Format,cin.name as Cinema,cin.address as Address " +
                "from Session.Sessions " +
                "join (select title,idMovie from Movie.Movies) as mov on Session.Sessions.idMovie = mov.idMovie " +
                "join (select name,idFormat from Session.Formats) as form on form.idFormat = Session.Sessions.idFormat " +
                "join (select name,address,idCinema from Cinema.Cinemas) as cin on cin.idCinema = Session.Sessions.idCinema " ;

        String cinemaNameMin = "";
        ArrayList<String> cinemasList= new ArrayList<>();

        String MpriceQuery = defaultQuery +
                " join (select MIN(price) as min from Session.Sessions) as pri on pri.min = price " +
                " where mov.title =? ;";

        String RpriceQuery = defaultQuery +
                " where mov.title =? and Session.Sessions.price >=? and Session.Sessions.price <=? order by time;";

        if (strPriceDraft.equals("cheapest")) {
            int k = 0;int i = 0;double distanceMin = 0.0, dist;String closestCinema = "";
            try {
                final PreparedStatement preparedStatement = connetion.getPreparedStatement(MpriceQuery);
                preparedStatement.setString(1, movieName);//mov.title
                final ResultSet result = preparedStatement.executeQuery();
                while (result.next()) {
                    String[] strA = new String[2];
                    strA[0] = result.getString("Cinema");
                    strA[1] = result.getString("Address");
                    //new String[4];
                    //strArray[2] =  result.getString("lat");
                    //strArray[3] =  result.getString("lng");
                    dist = BotHandlers.distFrom(userId, strA[1]);
                    i++;
                    if (i == 1) {
                        distanceMin = BotHandlers.distFrom(userId, strA[1]);
                    }
                    if (dist <= distanceMin) {
                        distanceMin = dist;
                        closestCinema = strA[0];
                    }
                }
                result.beforeFirst();
                while (result.next()) {
                    String[] strArray = new String[5];
                    strArray[0] = result.getString("Price");
                    strArray[1] = result.getString("Time");
                    strArray[2] = result.getString("Format");
                    strArray[3] = result.getString("Cinema");
                    strArray[4] = result.getString("Address");
                    //new String[7];
                    //strArray[5] =  result.getString("lat");
                    //strArray[6] =  result.getString("lng");
                    if(strDistDraft.equals("closest")) {
                        if (strArray[3].equals(closestCinema)) {
                            if(checkCurrentTime(strArray[1])) {
                                k++;
                                hashSessions.put(k, strArray);
                            }
                        }
                    }
                    else {
                        String[] distArr = strDistDraft.split("-");
                        sleep(900);
                        double n = BotHandlers.distFrom(userId, strArray[4]) / 1000;
                        if ((n >= Double.parseDouble(distArr[0])) && (n <= Double.parseDouble(distArr[1]))) {
                            if(checkCurrentTime(strArray[1])) {
                                k++;
                                hashSessions.put(k, strArray);
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            String[] priceRange = strPriceDraft.split("-");

            int k = 0;int i = 0;double distanceMin = 0.0, dist;String closestCinema = "";
            try {
                final PreparedStatement preparedStatement = connetion.getPreparedStatement(RpriceQuery);
                preparedStatement.setString(1, movieName);//mov.title
                preparedStatement.setString(2, priceRange[0]);//min price
                preparedStatement.setString(3, priceRange[1]);//max price
                final ResultSet result = preparedStatement.executeQuery();
                while (result.next()) {
                    String[] strA = new String[2];
                    strA[0] = result.getString("Cinema");
                    strA[1] = result.getString("Address");

                    dist = BotHandlers.distFrom(userId, strA[1]);
                    i++;
                    if (i == 1) {
                        distanceMin = BotHandlers.distFrom(userId, strA[1]);
                    }
                    if (dist <= distanceMin) {
                        distanceMin = dist;
                        closestCinema = strA[0];
                    }
                }
                result.beforeFirst();
                while (result.next()) {
                    String[] strArray = new String[5];
                    strArray[0] = result.getString("Price");
                    strArray[1] = result.getString("Time");
                    strArray[2] = result.getString("Format");
                    strArray[3] = result.getString("Cinema");
                    strArray[4] = result.getString("Address");

                    if(strDistDraft.equals("closest")) {
                        if (strArray[3].equals(closestCinema)) {
                            if(checkCurrentTime(strArray[1])) {
                                k++;
                                System.out.println("Got it!");
                                hashSessions.put(k, strArray);
                            }
                        }
                    }
                    else {
                        String[] distArr = strDistDraft.split("-");
                        sleep(901);
                        double n = BotHandlers.distFrom(userId, strArray[4]) / 1000;
                        if ((n >= Double.parseDouble(distArr[0])) && (n <= Double.parseDouble(distArr[1]))) {
                            if(checkCurrentTime(strArray[1])) {
                                k++;
                                hashSessions.put(k, strArray);
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        return hashSessions;
    }

    public Integer countEntries(String name){
        Integer n = 0;

        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement("SELECT COUNT(idMovie) FROM Movie.Movies");
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                n = result.getInt("COUNT(idMovie)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return n;
    }

    public HashMap<Integer,String[]> getMovies(){

        HashMap<Integer,String[]> hashMovies = new HashMap<>();

        Integer i=0;

        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement("SELECT * FROM Movie.Movies");
            final ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                i++;
                String[] strArray = new String[5];
                strArray[0] = result.getString("idMovie");
                strArray[1] = result.getString("title");
                strArray[2] = result.getString("description");
                try {
                    final PreparedStatement preparedStatement2 = connetion.getPreparedStatement("SELECT name FROM Movie.Genres,Movie.Genres_Movies,Movie.Movies WHERE Movie.Genres.idGenre=Movie.Genres_Movies.idGenre AND Movie.Genres_Movies.idMovie = Movie.Movies.idMovie AND Movie.Movies.title =?;");
                    preparedStatement2.setString(1, strArray[1]);
                    final ResultSet result2 = preparedStatement2.executeQuery();
                    if (result2.next()) {
                        strArray[4] = result2.getString("name");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                hashMovies.put(i,strArray);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hashMovies;
    }

    public Integer getSessionsState(Integer userId){
        Integer state = 0;

        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement("SELECT sessions_state FROM users WHERE user_id = ?");
            preparedStatement.setInt(1, userId);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                state = result.getInt("sessions_state");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return state;
    }

    public boolean setSessionsState(Integer userId, int state) {
        int updatedRows = 0;
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement("INSERT INTO users (user_id, chat_id, sessions_state) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE sessions_state=?");
            preparedStatement.setInt(1, userId);
            preparedStatement.setLong(2, userId);
            preparedStatement.setInt(3, state);
            preparedStatement.setInt(4, state);
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedRows > 0;
    }

    public Integer getMovieState(Integer userId){
        Integer state = 0;

        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement("SELECT movie_state FROM users WHERE user_id = ?");
            preparedStatement.setInt(1, userId);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                state = result.getInt("movie_state");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return state;
    }

    public boolean setMovieState(Integer userId, int state) {
        int updatedRows = 0;
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement("INSERT INTO users (user_id, chat_id, movie_state) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE movie_state=?");
            preparedStatement.setInt(1, userId);
            preparedStatement.setLong(2, userId);
            preparedStatement.setInt(3, state);
            preparedStatement.setInt(4, state);
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedRows > 0;
    }

    public int getUserState(Integer userId) {
        int state = 0;
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement("SELECT state FROM users WHERE user_id = ?");
            preparedStatement.setInt(1, userId);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                state = result.getInt("state");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return state;
    }

    public boolean setUserState(Integer userId, Long chatId, int state) {
        int updatedRows = 0;
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement("INSERT INTO users (user_id, chat_id, state) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE state=?");
            preparedStatement.setInt(1, userId);
            preparedStatement.setLong(2, chatId);
            preparedStatement.setInt(3, state);
            preparedStatement.setInt(4, state);
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedRows > 0;
    }

    public double[] getLocation(Integer userId) {
        double[] location = new double[2];
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement("SELECT lat,lon FROM users WHERE user_id = ?");
            preparedStatement.setInt(1, userId);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                location[0] = result.getDouble("lat");
                location[1] = result.getDouble("lon");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return location;
    }

    public boolean setLocation(Integer userId, Long chatId, Double lat,Double lon) {

        int updatedRows = 0;
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement
                    ("INSERT INTO users (user_id, chat_id, lat, lon) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE lat=?, lon=?");
            preparedStatement.setInt(1, userId);
            preparedStatement.setLong(2, chatId);
            preparedStatement.setDouble(3, lat);
            preparedStatement.setDouble(4, lon);
            preparedStatement.setDouble(5, lat);
            preparedStatement.setDouble(6, lon);
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedRows > 0;
    }


    public String getTypedLocation(Integer userId){
        String address = "";

        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement("SELECT street,build FROM users WHERE user_id = ?");
            preparedStatement.setInt(1, userId);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                address = result.getString("street");
                address += "," + String.valueOf(result.getInt("build"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return address;
    }

    public boolean setTypedLocation(Integer userId, Long chatId, String address) {

        String[] str = address.split(",");
        int updatedRows = 0;
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement
                    ("INSERT INTO users (user_id, chat_id, street, build) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE street=?, build=?");
            preparedStatement.setInt(1, userId);
            preparedStatement.setLong(2, chatId);
            preparedStatement.setString(3, str[0]);
            preparedStatement.setInt(4, Integer.parseInt(str[1]));
            preparedStatement.setString(5, str[0]);
            preparedStatement.setInt(6, Integer.parseInt(str[1]));
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return updatedRows > 0;
    }

    public String getPrice(Integer userId) {
        String str = new String();

        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement("SELECT price FROM users WHERE user_id = ?");
            preparedStatement.setInt(1, userId);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                str = result.getString("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return str;
    }

    public boolean setPrice(Integer userId, Long chatId, String price) {

        int updatedRows = 0;
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement
                    ("INSERT INTO users (user_id, chat_id, price) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE price=?");
            preparedStatement.setInt(1, userId);
            preparedStatement.setLong(2, chatId);
            preparedStatement.setString(3, price);
            preparedStatement.setString(4, price);
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return updatedRows > 0;
    }

    public String getDistance(Integer userId) {

        String str = new String();

        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement("SELECT dist FROM users WHERE user_id = ?");
            preparedStatement.setInt(1, userId);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                str = result.getString("dist");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return str;
    }

    public boolean setDistance(Integer userId, Long chatId, String distance) {

        int updatedRows = 0;
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement
                    ("INSERT INTO users (user_id, chat_id, dist) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE dist=?");
            preparedStatement.setInt(1, userId);
            preparedStatement.setLong(2, chatId);
            preparedStatement.setString(3, distance);
            preparedStatement.setString(4, distance);
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return updatedRows > 0;
    }

    @Deprecated
    public List<String> getrecentMovies() {
        List<String> recentMovies = new ArrayList<>();
        recentMovies.add("Suicide Squad");
        recentMovies.add("Star Trek");
        recentMovies.add("Jason Bourne");
        return recentMovies;
    }

}
