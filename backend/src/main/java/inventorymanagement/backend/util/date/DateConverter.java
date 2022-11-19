package inventorymanagement.backend.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    private static final SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    public static Date dateFromString(String s){
        try {
            return sdt.parse(s);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String stringFromDate(Date d) {
        return sdt.format(d);
    }
}
