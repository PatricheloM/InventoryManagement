package inventorymanagement.backend.controller;

import inventorymanagement.backend.util.InventoryManagementStringTools;
import inventorymanagement.backend.util.ResponseEntityFactory;
import inventorymanagement.backend.util.auth.NoAuthorizationRequired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/base")
public class BaseController {

    protected final String PATH = this.getClass().getAnnotation(RequestMapping.class).value()[0];

    @NoAuthorizationRequired
    @GetMapping(value = "/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntityFactory.produce(
                InventoryManagementStringTools.getPongMsg(),
                HttpStatus.OK, PATH + "/ping"
        );
    }
}
