package inventorymanagement.backend.controller;

import inventorymanagement.backend.util.InventoryManagementStringTools;
import inventorymanagement.backend.util.ResponseEntityFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/base")
public class BaseController {

    protected final String PATH = this.getClass().getAnnotation(RequestMapping.class).value()[0];

    @GetMapping(value = "/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntityFactory.produce(
                InventoryManagementStringTools.getPongMsg(),
                HttpStatus.OK, PATH + "/ping"
        );
    }
}
