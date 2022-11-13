package inventorymanagement.backend.util;

import inventorymanagement.backend.dto.MessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityFactory {
    public static ResponseEntity<Object> produce(String msg, HttpStatus httpStatus, String path) {
        return new ResponseEntity<>(new MessageDTO(
                msg, httpStatus, path), httpStatus);
    }
}
