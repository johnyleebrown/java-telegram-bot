package services;

import org.telegram.telegrambots.api.objects.Message;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by Greg on 10/25/16.
 */
public class Inspections {

    public static boolean checkTypedLocation(Message message){

        if(!message.getText().contains(",")) return false;

        String pat1 = "^([A-Za-z]){4,25},(\\d{1,3})$";
        String pat2 = "^([1-9\\s]){1,3}([A-Za-z]){4,25},(\\d{1,3})$";

        if(Pattern.matches(pat1,message.getText()) || Pattern.matches(pat2,message.getText())) {
            try {
                String[] str = message.getText().split(",");
                int n = Integer.parseInt(str[1]);
                if (n < 0 || n > 380) return false;
            } catch (NumberFormatException e) {
                return false;
            }
        } else return false;

        return true;
    }

    public static boolean checkPriceRange(Message message){

        if(!message.getText().contains("-")) return false;

        String pat = "^(\\d{1,4})-(\\d{1,4})$";

        if(Pattern.matches(pat,message.getText())){
            try {
                String[] str = message.getText().split("-");
                int x = Integer.parseInt(str[0]);
                int y = Integer.parseInt(str[1]);
                if (x < 80 || x > 2000 || y < 80 || y > 2000 ) return false;
                if ( x >= y ) return false;
            } catch (NumberFormatException e) {
                return false;
            }
        } else return false;

        return true;
    }

    public static boolean checkDistRange(Message message){

        String pat1 = "^(\\d){1,2}.(\\d){1,2}-(\\d){1,2}.(\\d){1,2}$";
        String pat2 = "^(\\d){1,2}.(\\d){1,2}-(\\d){1,2}$";
        String pat3 = "^(\\d){1,2}-(\\d){1,2}.(\\d){1,2}$";
        String pat4 = "^(\\d){1,2}-(\\d){1,2}$";


        if(message.getText().contains("-")) {
            if (Pattern.matches(pat1, message.getText()) || Pattern.matches(pat2, message.getText())
                    || Pattern.matches(pat3, message.getText()) || Pattern.matches(pat4, message.getText())) {
                try {
                    String[] str = message.getText().split("-");
                    double x = Double.parseDouble(str[0]);
                    double y = Double.parseDouble(str[1]);
                    if (x < 0.1 || x > 49 || y < 0.1 || y > 50) return false;
                    if (x >= y) return false;
                } catch (NumberFormatException e) {
                    return false;
                }
            } else return false;
        } else return false;

        return true;
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

}
