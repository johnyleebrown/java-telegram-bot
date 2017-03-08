package database;

import org.telegram.telegrambots.logging.BotLogger;
import services.Distance;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static java.lang.Thread.sleep;
import static services.Inspections.checkCurrentTime;
import static services.Queries.*;


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
                instance = new Manager();
                currentInstance = instance;
            }
        } else {
            currentInstance = instance;
        }
        return currentInstance;
    }

    /**
     * Get cinemas and their addresses
     * @returns HashMap<String,String> hashCinemas
     */

    private static HashMap<String,String> getCinemas(){

        String cinemaName ="";
        String cinemaAddress ="";
        HashMap<String,String> hashCinemas = new HashMap<>();
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(getCinemasQuery);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                cinemaName = result.getString("name");
                cinemaAddress = result.getString("address");
                hashCinemas.put(cinemaName,cinemaAddress);
            }
        } catch (SQLException e) {
            BotLogger.info(LOGTAG,e);
        }
        return hashCinemas;
    }

    /**
     * Get users distance and price preference from users database
     * @returns strDistDraft, strPriceDraft
     */

    private static String[] getPreferences(int userId){

        String[] strDistDraft = new String[2];
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(getPreferencesQuery);
            preparedStatement.setInt(1, userId);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                strDistDraft[0] = result.getString("dist");
                strDistDraft[1] = result.getString("price");
            }
        } catch (SQLException e) {
            BotLogger.info(LOGTAG,e);
        }

        return strDistDraft;

    }

    /**
     * Searches sessions by distance and price
     *
     * Finds closest cinema
     * @returns cinemaNameMin
     * Find cinemas in range
     * @returns cinemasList
     */

    public static HashMap<Integer,String[]> getSessions(String movieName, int userId){

        String strDistDraft = getPreferences(userId)[0];
        String strPriceDraft =  getPreferences(userId)[1];
        HashMap<Integer,String[]> hashSessions = new HashMap<>();

        if (strPriceDraft.equals("cheapest")) {
            int k = 0;int i = 0;double distanceMin = 0.0, dist;String closestCinema = "";
            try {
                final PreparedStatement preparedStatement = connetion.getPreparedStatement(MPriceQuery);
                preparedStatement.setString(1, movieName);//mov.title
                final ResultSet result = preparedStatement.executeQuery();
                while (result.next()) {
                    String[] strA = new String[2];
                    strA[0] = result.getString("Cinema");
                    strA[1] = result.getString("Address");
                    dist = Distance.distFrom(userId, strA[1]);
                    i++;
                    if (i == 1) {
                        distanceMin = Distance.distFrom(userId, strA[1]);
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
                    if( strDistDraft.equals("closest")
                        && (strArray[3].equals(closestCinema))
                        && (checkCurrentTime(strArray[1]))) {
                            k++;
                            hashSessions.put(k, strArray);
                    }
                    else {
                        String[] distArr = strDistDraft.split("-");
                        sleep(900);
                        double n = Distance.distFrom(userId, strArray[4]) / 1000;
                        if ((n >= Double.parseDouble(distArr[0])) && (n <= Double.parseDouble(distArr[1]))) {
                            if(checkCurrentTime(strArray[1])) {
                                k++;
                                hashSessions.put(k, strArray);
                            }
                        }
                    }
                }
            } catch (SQLException | InterruptedException e) {
                BotLogger.info(LOGTAG+" getSessions",e);
            }
        }
        else {
            String[] priceRange = strPriceDraft.split("-");
            int k = 0;int i = 0;double distanceMin = 0.0, dist;String closestCinema = "";
            try {
                final PreparedStatement preparedStatement = connetion.getPreparedStatement(RPriceQuery);
                preparedStatement.setString(1, movieName);//mov.title
                preparedStatement.setString(2, priceRange[0]);//min price
                preparedStatement.setString(3, priceRange[1]);//max price
                final ResultSet result = preparedStatement.executeQuery();
                while (result.next()) {
                    String[] strA = new String[2];
                    strA[0] = result.getString("Cinema");
                    strA[1] = result.getString("Address");

                    dist = Distance.distFrom(userId, strA[1]);
                    i++;
                    if (i == 1) {
                        distanceMin = Distance.distFrom(userId, strA[1]);
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

                    if( strDistDraft.equals("closest")
                        && (strArray[3].equals(closestCinema))
                        && (checkCurrentTime(strArray[1]))) {
                            k++;
                            System.out.println("Got it!");
                            hashSessions.put(k, strArray);
                    }
                    else {
                        String[] distArr = strDistDraft.split("-");
                        sleep(900);
                        double n = Distance.distFrom(userId, strArray[4]) / 1000;
                        if ((n >= Double.parseDouble(distArr[0])) && (n <= Double.parseDouble(distArr[1]))) {
                            if(checkCurrentTime(strArray[1])) {
                                k++;
                                hashSessions.put(k, strArray);
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                BotLogger.info(LOGTAG+" getSessions",e);
            } catch (InterruptedException e) {
                BotLogger.info(LOGTAG+" getSessions",e);
            }

        }

        return hashSessions;
    }

    public Integer countEntries(String name){
        Integer n = 0;

        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(countEntriesQuery);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                n = result.getInt("COUNT(idMovie)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return n;
    }

    public HashMap<Integer,String[]> getMovies(int userId){
        HashMap<Integer,String[]> hashMovies = new HashMap<>();

        Integer i=0;

        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(getAllMovies);
            final ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                i++;
                String[] strArray = new String[5];
                strArray[0] = result.getString("idMovie");
                strArray[1] = result.getString("title");
                strArray[2] = result.getString("description");
                try {
                    final PreparedStatement preparedStatement2 = connetion.getPreparedStatement(getMoviesQuery);
                    preparedStatement2.setString(1, strArray[1]);
                    final ResultSet result2 = preparedStatement2.executeQuery();
                    if (result2.next()) {
                        strArray[4] = result2.getString("name");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                for(String arr : getRestrictions(userId)){
                    if (!arr.equals(strArray[1])) {
                        System.out.println("1="+strArray[1]);
                        hashMovies.put(i, strArray);
                    }
                }
            }
        } catch (SQLException e) {
            BotLogger.info(LOGTAG,e);
        }

        return hashMovies;
    }

    public Integer getSessionsState(Integer userId){
        Integer state = 0;

        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(sessionsStateQuery);
            preparedStatement.setInt(1, userId);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                state = result.getInt("sessions_state");
            }
        } catch (SQLException e) {
            BotLogger.info(LOGTAG,e);
        }
        return state;
    }

    public boolean setSessionsState(Integer userId, int state) {
        int updatedRows = 0;
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(setSessionsStateQuery);
            preparedStatement.setInt(1, userId);
            preparedStatement.setLong(2, userId);
            preparedStatement.setInt(3, state);
            preparedStatement.setInt(4, state);
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.info(LOGTAG,e);
        }
        return updatedRows > 0;
    }

    public Integer getMovieState(Integer userId){
        Integer state = 0;

        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(movieStateQuery);
            preparedStatement.setInt(1, userId);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                state = result.getInt("movie_state");
            }
        } catch (SQLException e) {
            BotLogger.info(LOGTAG,e);
        }
        return state;
    }

    public boolean setMovieState(Integer userId, int state) {
        int updatedRows = 0;
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(setMovieStateQuery);
            preparedStatement.setInt(1, userId);
            preparedStatement.setLong(2, userId);
            preparedStatement.setInt(3, state);
            preparedStatement.setInt(4, state);
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.info(LOGTAG,e);
        }
        return updatedRows > 0;
    }

    public int getUserState(Integer userId) {
        int state = 0;
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(getUserStateQuery);
            preparedStatement.setInt(1, userId);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                state = result.getInt("state");
            }
        } catch (SQLException e) {
            BotLogger.info(LOGTAG,e);
        }
        return state;
    }

    public boolean setUserState(Integer userId, Long chatId, int state) {
        int updatedRows = 0;
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(setUserStateQuery);
            preparedStatement.setInt(1, userId);
            preparedStatement.setLong(2, chatId);
            preparedStatement.setInt(3, state);
            preparedStatement.setInt(4, state);
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.info(LOGTAG,e);
        }
        return updatedRows > 0;
    }

    public double[] getLocation(Integer userId) {
        double[] location = new double[2];
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(getLocation);
            preparedStatement.setInt(1, userId);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                location[0] = result.getDouble("lat");
                location[1] = result.getDouble("lon");
            }
        } catch (SQLException e) {
            BotLogger.info(LOGTAG,e);
        }
        return location;
    }

    public boolean setLocation(Integer userId, Long chatId, Double lat,Double lon) {

        int updatedRows = 0;
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(setLocation);
            preparedStatement.setInt(1, userId);
            preparedStatement.setLong(2, chatId);
            preparedStatement.setDouble(3, lat);
            preparedStatement.setDouble(4, lon);
            preparedStatement.setDouble(5, lat);
            preparedStatement.setDouble(6, lon);
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.info(LOGTAG,e);
        }
        return updatedRows > 0;
    }


    public String getTypedLocation(Integer userId){
        String address = "";

        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(getTypedLocation);
            preparedStatement.setInt(1, userId);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                address = result.getString("street");
                address += "," + String.valueOf(result.getInt("build"));
            }
        } catch (SQLException e) {
            BotLogger.info(LOGTAG,e);
        }
        return address;
    }

    public boolean setTypedLocation(Integer userId, Long chatId, String address) {

        String[] str = address.split(",");
        int updatedRows = 0;
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(setTypedLocation);
            preparedStatement.setInt(1, userId);
            preparedStatement.setLong(2, chatId);
            preparedStatement.setString(3, str[0]);
            preparedStatement.setInt(4, Integer.parseInt(str[1]));
            preparedStatement.setString(5, str[0]);
            preparedStatement.setInt(6, Integer.parseInt(str[1]));
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.info(LOGTAG,e);
        }

        return updatedRows > 0;
    }

    public String getPrice(Integer userId) {
        String str = new String();

        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(getPrice);
            preparedStatement.setInt(1, userId);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                str = result.getString("price");
            }
        } catch (SQLException e) {
            BotLogger.info(LOGTAG,e);
        }

        return str;
    }

    public boolean setPrice(Integer userId, Long chatId, String price) {

        int updatedRows = 0;
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(setPrice);
            preparedStatement.setInt(1, userId);
            preparedStatement.setLong(2, chatId);
            preparedStatement.setString(3, price);
            preparedStatement.setString(4, price);
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.info(LOGTAG,e);
        }

        return updatedRows > 0;
    }

    public String getDistance(Integer userId) {

        String str = new String();

        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(getDistance);
            preparedStatement.setInt(1, userId);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                str = result.getString("dist");
            }
        } catch (SQLException e) {
            BotLogger.info(LOGTAG,e);
        }

        return str;
    }

    public boolean setDistance(Integer userId, Long chatId, String distance) {

        int updatedRows = 0;
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(setDistance);
            preparedStatement.setInt(1, userId);
            preparedStatement.setLong(2, chatId);
            preparedStatement.setString(3, distance);
            preparedStatement.setString(4, distance);
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.info(LOGTAG,e);
        }

        return updatedRows > 0;
    }

    public boolean setLastSeen(Integer userId, Long chatId, String lastseen) {

        int updatedRows = 0;
        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(setLastSeen);
            preparedStatement.setInt(1, userId);
            preparedStatement.setLong(2, chatId);
            preparedStatement.setString(3, lastseen);
            preparedStatement.setString(4, lastseen);
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            BotLogger.info(LOGTAG,e);
        }

        return updatedRows > 0;
    }

    public String[] getRestrictions(Integer userId) {

        String str = new String();

        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(getRestrictions);
            preparedStatement.setInt(1, userId);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                str = result.getString("restricted");
            }
        } catch (SQLException e) {
            BotLogger.info(LOGTAG,e);
        }

        String restMoviesArray[] = str.split("#");

        return restMoviesArray;
    }

    public String[] getVenueInfo(Integer userId) {

        String str = new String();

        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(getVenueInfo);
            preparedStatement.setInt(1, userId);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                str = result.getString("venue_info");
            }
        } catch (SQLException e) {
            BotLogger.info(LOGTAG,e);
        }

        String venue_info[] = str.split("#");

        return venue_info;
    }

    public int getFlag(Integer userId) {
        int num = 0;

        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(getFlag);
            preparedStatement.setInt(1, userId);
            final ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                num = result.getInt("chosenflag");
            }
        } catch (SQLException e) {
            BotLogger.info(LOGTAG,e);
        }

        return num;
    }

    public void setFlag(Integer userId,int num) {

        try {
            final PreparedStatement preparedStatement = connetion.getPreparedStatement(setFlag);
            preparedStatement.setInt(1, userId);
            preparedStatement.setLong(2, userId);
            preparedStatement.setInt(3, num);
            preparedStatement.setInt(4, num);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
