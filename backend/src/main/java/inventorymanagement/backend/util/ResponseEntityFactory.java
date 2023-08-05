package inventorymanagement.backend.util;

import inventorymanagement.backend.dto.MessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ResponseEntityFactory {

    private ResponseEntityFactory(){}

    public static ResponseEntity<?> produce(String msg, HttpStatus httpStatus, String path) {
        return new ResponseEntity<>(new MessageDTO(
                msg, httpStatus, path), httpStatus);
    }
}
