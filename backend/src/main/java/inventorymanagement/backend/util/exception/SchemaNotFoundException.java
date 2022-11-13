package inventorymanagement.backend.util.exception;

public class SchemaNotFoundException extends Throwable {

    public SchemaNotFoundException(String msg) {
        super(msg);
    }

    public SchemaNotFoundException(Exception e) {
        super(e);
    }

    public SchemaNotFoundException() {
        super();
    }
}
