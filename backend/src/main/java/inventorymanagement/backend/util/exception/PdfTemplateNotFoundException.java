package inventorymanagement.backend.util.exception;

public class PdfTemplateNotFoundException extends RuntimeException {

    public PdfTemplateNotFoundException(String msg) {
        super(msg);
    }

    public PdfTemplateNotFoundException(Exception e) {
        super(e);
    }

    public PdfTemplateNotFoundException() {
        super();
    }
}
