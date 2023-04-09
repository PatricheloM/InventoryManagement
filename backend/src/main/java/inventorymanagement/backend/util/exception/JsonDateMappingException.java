package inventorymanagement.backend.util.exception;

public class JsonDateMappingException extends RuntimeException {

    public JsonDateMappingException(String msg) {
        super(msg);
    }

    public JsonDateMappingException(Exception e) {
        super(e);
    }

    public JsonDateMappingException() {
        super();
    }
}
