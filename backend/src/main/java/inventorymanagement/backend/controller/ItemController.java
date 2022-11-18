package inventorymanagement.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import inventorymanagement.backend.dto.ItemDTO;
import inventorymanagement.backend.service.ItemService;
import inventorymanagement.backend.util.InventoryManagementStringTools;
import inventorymanagement.backend.util.ResponseEntityFactory;
import inventorymanagement.backend.util.auth.Authorization;
import inventorymanagement.backend.util.auth.AuthorizationCheck;
import inventorymanagement.backend.util.date.DateConverter;
import inventorymanagement.backend.util.enums.AccountPrivilege;
import inventorymanagement.backend.util.exception.SchemaNotFoundException;
import inventorymanagement.backend.util.json.JsonFactory;
import inventorymanagement.backend.util.validator.JsonValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/item")
public class ItemController {

    private static final String PATH = "/api/item";

    @Autowired
    protected ModelMapper modelMapper;

    @Autowired
    ItemService itemService;

    @Autowired
    AuthorizationCheck authorizationCheck;

    @Authorization(privileges = { AccountPrivilege.ADMIN, AccountPrivilege.IMPORTER})
    @PostMapping(value = "/importing", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> importing(@RequestBody Object object, @RequestParam String token) {
        if (authorizationCheck.check(new Object(){}.getClass().getEnclosingMethod(), token)) {
            try {
                if (JsonValidator.validate(JsonFactory.produce(object), ItemDTO.class)) {
                    ItemDTO item = modelMapper.map(object, ItemDTO.class);
                    itemService.saveItem(item);
                    return ResponseEntityFactory.produce(InventoryManagementStringTools.getItemAddedMsg(),
                            HttpStatus.OK, PATH + "/importing");
                } else
                    return ResponseEntityFactory.produce(InventoryManagementStringTools.getBadRequestMsg(),
                            HttpStatus.BAD_REQUEST, PATH + "/importing");
            } catch (SchemaNotFoundException | JsonProcessingException e) {
                return ResponseEntityFactory.produce(InventoryManagementStringTools.getInternalServerErrorMsg(),
                        HttpStatus.INTERNAL_SERVER_ERROR, PATH + "/importing");
            }
        } else {
            return ResponseEntityFactory.produce(InventoryManagementStringTools.getUnauthorizedMsg(),
                    HttpStatus.UNAUTHORIZED, PATH + "/importing");
        }
    }

    @Authorization(privileges = { AccountPrivilege.ADMIN,
            AccountPrivilege.EXPORTER, AccountPrivilege.IMPORTER,
            AccountPrivilege.IMPORTER_EXPORTER, AccountPrivilege.MAINTENANCE})
    @GetMapping(value = "/{itemId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getItem(@PathVariable("itemId") int itemId, @RequestParam String token) {
        if (authorizationCheck.check(new Object(){}.getClass().getEnclosingMethod(), token)) {
            Optional<ItemDTO> item = itemService.fetchItem(itemId);
            return item.<ResponseEntity<Object>>map(itemDTO -> new ResponseEntity<>(itemDTO, HttpStatus.OK))
                    .orElseGet(() -> ResponseEntityFactory.produce(InventoryManagementStringTools.getItemNotFoundMsg(),
                    HttpStatus.NOT_FOUND, PATH + "/" + itemId));
        } else {
            return ResponseEntityFactory.produce(InventoryManagementStringTools.getUnauthorizedMsg(),
                    HttpStatus.UNAUTHORIZED, PATH + "/" + itemId);
        }
    }
}
