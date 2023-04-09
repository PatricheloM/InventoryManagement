package inventorymanagement.backend.util.date;

import inventorymanagement.backend.util.exception.JsonDateMappingException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    private final SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    public Date dateFromString(String s){
        try {
            return sdt.parse(s);
        } catch (ParseException e) {
            throw new JsonDateMappingException(e);
        }
    }

    public String stringFromDate(Date d) {
        return sdt.format(d);
    }
}
