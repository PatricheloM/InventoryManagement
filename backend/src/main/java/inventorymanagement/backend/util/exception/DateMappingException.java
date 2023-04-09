package inventorymanagement.backend.util.exception;

public class DateMappingException extends RuntimeException {

    public DateMappingException(String msg) {
        super(msg);
    }

    public DateMappingException(Exception e) {
        super(e);
    }

    public DateMappingException() {
        super();
    }
}
