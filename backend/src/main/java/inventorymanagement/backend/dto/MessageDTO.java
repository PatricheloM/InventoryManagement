package inventorymanagement.backend.dto;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class MessageDTO  implements Serializable {

    private final ZonedDateTime timestamp;
    private final int status;
    private final String message;
    private final String path;

    public MessageDTO(String message, HttpStatus httpStatus, String path)
    {
        timestamp = ZonedDateTime.now();
        status = httpStatus.value();
        this.message = message;
        this.path = path;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}
