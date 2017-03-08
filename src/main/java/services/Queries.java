package services;

/**
 * Created by Greg on 10/26/16.
 */
public class Queries {

    public static final String defaultQuery = "select Session.sessions.price as Price,Session.sessions.time as Time,form.name as Format,cin.name as Cinema,cin.address as Address " +
            "from Session.Sessions " +
            "join (select title,idMovie from movie.Movies) as mov on session.sessions.idMovie = mov.idMovie " +
            "join (select name,idFormat from Session.Formats) as form on form.idFormat = Session.Sessions.idFormat " +
            "join (select name,address,idCinema from Cinema.Cinemas) as cin on cin.idCinema = Session.Sessions.idCinema " ;

    public static final String MPriceQuery = defaultQuery + " join (select MIN(price) as min from session.sessions) as pri on pri.min = price " + " where mov.title =? ;";

    public static final String RPriceQuery = defaultQuery + " where mov.title =? and Session.sessions.price >=? and Session.sessions.price <=? order by time;";

    public static final String getCinemasQuery = "select name,address from cinema.cinemas";

    public static final String getPreferencesQuery = "SELECT dist,price FROM users WHERE user_id = ?";

    public static final String countEntriesQuery="SELECT COUNT(idMovie) FROM Movie.Movies";

    public static final String getMoviesQuery="SELECT name FROM Movie.Genres,Movie.Genres_Movies,Movie.Movies WHERE Movie.Genres.idGenre=Movie.Genres_Movies.idGenre AND Movie.Genres_Movies.idMovie = Movie.Movies.idMovie AND Movie.Movies.title =?;";

    public static final String sessionsStateQuery="SELECT sessions_state FROM users WHERE user_id = ?";

    public static final String setSessionsStateQuery="INSERT INTO users (user_id, chat_id, sessions_state) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE sessions_state=?";

    public static final String movieStateQuery="SELECT movie_state FROM users WHERE user_id = ?";

    public static final String setMovieStateQuery="INSERT INTO users (user_id, chat_id, movie_state) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE movie_state=?";

    public static final String getUserStateQuery="SELECT state FROM users WHERE user_id = ?";

    public static final String setUserStateQuery="INSERT INTO users (user_id, chat_id, state) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE state=?";

    public static final String getLocation="SELECT lat,lon FROM users WHERE user_id = ?";

    public static final String setLocation="INSERT INTO users (user_id, chat_id, lat, lon) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE lat=?, lon=?";

    public static final String getTypedLocation="SELECT street,build FROM users WHERE user_id = ?";

    public static final String setTypedLocation="INSERT INTO users (user_id, chat_id, street, build) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE street=?, build=?";

    public static final String getPrice="SELECT price FROM users WHERE user_id = ?";

    public static final String setPrice="INSERT INTO users (user_id, chat_id, price) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE price=?";

    public static final String getDistance="SELECT dist FROM users WHERE user_id = ?";

    public static final String setDistance="INSERT INTO users (user_id, chat_id, dist) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE dist=?";

    public static final String setLastSeen="INSERT INTO users (user_id, chat_id, lastseen) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE lastseen=?";

    public static final String getRestrictions="SELECT restricted FROM users WHERE user_id = ?";

    public static final String getVenueInfo= "SELECT venue_info FROM users WHERE user_id = ?";

    public static final String getFlag="SELECT chosenflag FROM users WHERE user_id = ?";

    public static final String setFlag ="INSERT INTO users (user_id, chat_id, chosenflag) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE chosenflag=?";

    public static final String getAllMovies="SELECT * FROM Movie.Movies";

}
