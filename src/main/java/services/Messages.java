package services;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Greg on 10/25/16.
 */
public class Messages {

    public static final String noSessionsMessage(String movieName) {
        return String.format("%s No sessions was found for %s %s\n%s Try another movie or change the parameters in %sSettings",
                Emoji.EXCLWARNING.toString(),movieName,Emoji.CRYING_FACE.toString(),
                Emoji.HAPPY.toString(),Emoji.SETT.toString()
        );
    }

    public static final String getSessionMessage(String movieName, String format, String movieTime, String price, double dist, double travelTime, String cinema){
        String distance = "";
        if(dist<1000){
            NumberFormat formatter = new DecimalFormat("#0");
            distance = formatter.format(dist) + " meters";
        } else {
            NumberFormat formatter = new DecimalFormat("#0.0");
            distance = formatter.format(dist/1000) + " km";
        }
        String traveltim = "";
        if(travelTime>60){
            NumberFormat formatter = new DecimalFormat("#0.0");
            traveltim = formatter.format(travelTime/60) + " h";
        } else {
            traveltim = (int)travelTime + " min";
        }
        return String.format("%s %s %s\n%s %s\n%s %s\n%s %s / %s %s\n%s %s",
                Emoji.CAMERAEM.toString(),movieName,format,
                Emoji.ALARM_CLOCK.toString(),movieTime,
                Emoji.CASH.toString(),price,
                Emoji.FINISH.toString(),distance,
                Emoji.WALKING.toString(),traveltim,
                Emoji.CASTLE.toString(),cinema
        );
    }

    public static final String getHelpMessage() {
        String baseString = "You need to know the closest %s :checkered_flag: and the cheapest %s movies sessions?\n" +
                "Just send this commands %s and you will receive all information instantly %s";
        return String.format(baseString, Emoji.FINISH.toString(),
                Emoji.CASH.toString(),
                Emoji.FINGDOWN.toString(), Emoji.OK.toString());
    }

    public static final String getPickALocationMessage() {
        String baseString = "Pick a location %s";
        return String.format(baseString,
                Emoji.WHITE_HEAVY_CHECK_MARK.toString());
    }

    public static final String getShowMoviesMessage() {
        String baseString = "Pick a movie below %s%s";
        return String.format(baseString,
                Emoji.FINGDOWN.toString(),
                Emoji.WHITE_HEAVY_CHECK_MARK.toString());
    }

    public static final String getSettingsMessage() {
        return String.format("%s Set up a bot ",
                Emoji.WRENCH.toString());
    }

    public static final String getBlockedUserMessage() {
        return ("You just played yourself!\nYou are blocked.");
    }

    public static final String getSettingsSetPrice() {
        return ("Sessions with the lowest price will be shown ");
    }

    public static final String getSettingsSetDistance() {
        return ("Closest sessions will be shown ");
    }

    public static final String getPriceMessage() {
        return ("Change price properties ");
    }

    public static final String getDistanceMessage() {
        return ("Change distance properties ");
    }

    public static final String getStoredLocationMessage() {
        return ("Save a new location or \nEdit a current one ");
    }

    public static final String getCelebMessage() {
        return String.format("We found a movie for you %s",
                Emoji.CELEB.toString());
    }

    public static final String getOptionMessage() {
        return String.format("Please, select an option from the menu.",
                Emoji.CELEB.toString());
    }

    public static final String getRangeMessage() {
        return "Type range (LOW-HIGH): ";
    }

    public static final String getDistRangeMessage() {
        return "Type an address(STREET,BUILDING): ";
    }

    public static final String getSendLocation() {
        return "Send us your location: ";
    }
}
