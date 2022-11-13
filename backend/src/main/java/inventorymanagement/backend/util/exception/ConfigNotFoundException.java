package inventorymanagement.backend.util.exception;

public class ConfigNotFoundException extends RuntimeException {

    public ConfigNotFoundException(String msg) {
        super(msg);
    }

    public ConfigNotFoundException(Exception e) {
        super(e);
    }

    public ConfigNotFoundException() {
        super();
    }
}
