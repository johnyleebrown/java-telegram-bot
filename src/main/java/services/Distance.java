package services;

import database.Manager;
import org.telegram.telegrambots.api.objects.Message;

/**
 * Created by Greg on 10/25/16.
 */
public class Distance {

    public static double distFrom(int userId, String cinemaAddress) {

        String str = Manager.getInstance().getTypedLocation(userId) + " ,Санкт-Петербург";

        //getCoord = lat,lng from param
        double[] cinema = ToCoordinates.getCoord(cinemaAddress + " ,Санкт-Петербург");
        double[] cord;
        cord = (Manager.getInstance().getFlag(userId) == 1)?(Manager.getInstance().getLocation(userId)):
                (ToCoordinates.getCoord(str));
        double earthRadius = 6371000; // 3958.75 miles ,6371.0 km
        double dLat = Math.toRadians(cinema[0]-cord[0]);
        double dLng = Math.toRadians(cinema[1]-cord[1]);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(cord[0])) * Math.cos(Math.toRadians(cinema[0]));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return earthRadius * c;
    }

    @Deprecated
    public static String getDirections(Message message, boolean flag){

        String link;
        String html;
        String beg = "https://www.google.ru/maps/dir/";
        String city = "Saint-Petersburg";
        String cp = ",";
        String sep = "/";
        String zoom = "17z";
        String end = "/?hl=ru";

        double[] cinema = new double[]{59.93166,30.353736};
        double[] coord = Manager.getInstance().getLocation(message.getFrom().getId());
        String typed = Manager.getInstance().getTypedLocation(message.getFrom().getId());

        link = (flag)?(beg + coord[0] + cp + coord[1] + sep + cinema[0] + cp + cinema[1] + end)
                :(beg + typed + cp + city + sep + cinema[0] + cp + cinema[1]);
        flag = false;
        html = "<a href=\"" + link + "\">Directions to the cinema</a>";

        return html;
    }
}
