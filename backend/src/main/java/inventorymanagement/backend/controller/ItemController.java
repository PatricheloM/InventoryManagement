package inventorymanagement.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import inventorymanagement.backend.dto.DateQueryDTO;
import inventorymanagement.backend.dto.ItemDTO;
import inventorymanagement.backend.dto.PdfDTO;
import inventorymanagement.backend.service.ItemService;
import inventorymanagement.backend.util.InventoryManagementStringTools;
import inventorymanagement.backend.util.ResponseEntityFactory;
import inventorymanagement.backend.util.auth.Authorization;
import inventorymanagement.backend.util.auth.AuthorizationCheck;
import inventorymanagement.backend.util.enums.AccountPrivilege;
import inventorymanagement.backend.util.enums.ImportExport;
import inventorymanagement.backend.util.exception.SchemaNotFoundException;
import inventorymanagement.backend.util.json.JsonFactory;
import inventorymanagement.backend.util.pdf.Base64Encoder;
import inventorymanagement.backend.util.pdf.PdfFactory;
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

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/item")
public class ItemController extends BaseController {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ItemService itemService;
    @Autowired
    AuthorizationCheck authorizationCheck;

    @Authorization(
            privileges = {
                    AccountPrivilege.ADMIN,
                    AccountPrivilege.IMPORTER,
                    AccountPrivilege.IMPORTER_EXPORTER
            }
    )
    @PostMapping(value = "/importing", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> importing(@RequestBody Object object, @RequestParam String token) {
        if (!authorizationCheck.check(this.getClass(), token)) {
            return ResponseEntityFactory.produce(
                    InventoryManagementStringTools.getUnauthorizedMsg(),
                    HttpStatus.UNAUTHORIZED, PATH + "/importing"
            );
        }

        try {
            if (!JsonValidator.validate(JsonFactory.produce(object), ItemDTO.class)) {
                return ResponseEntityFactory.produce(
                        InventoryManagementStringTools.getBadRequestMsg(),
                        HttpStatus.BAD_REQUEST, PATH + "/importing"
                );
            }
        } catch (SchemaNotFoundException | JsonProcessingException e) {
            return ResponseEntityFactory.produce(
                    InventoryManagementStringTools.getInternalServerErrorMsg(),
                    HttpStatus.INTERNAL_SERVER_ERROR, PATH + "/importing"
            );
        }

        ItemDTO item = modelMapper.map(object, ItemDTO.class);
        itemService.saveItem(item);
        PdfDTO pdf = new PdfDTO(Base64Encoder.encode(PdfFactory.produce(item, ImportExport.IMPORT)));

        return new ResponseEntity<>(pdf, HttpStatus.OK);
    }

    @Authorization(
            privileges = {
                    AccountPrivilege.ADMIN,
                    AccountPrivilege.EXPORTER,
                    AccountPrivilege.IMPORTER_EXPORTER
            }
    )
    @PostMapping(value = "/exporting", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> exporting(@RequestBody Object object, @RequestParam String token) {
        if (!authorizationCheck.check(this.getClass(), token)) {
            return ResponseEntityFactory.produce(
                    InventoryManagementStringTools.getUnauthorizedMsg(),
                    HttpStatus.UNAUTHORIZED, PATH + "/exporting"
            );
        }

        try {
            if (!JsonValidator.validate(JsonFactory.produce(object), ItemDTO.class)) {
                return ResponseEntityFactory.produce(
                        InventoryManagementStringTools.getBadRequestMsg(),
                        HttpStatus.BAD_REQUEST, PATH + "/exporting"
                );
            }

        } catch (SchemaNotFoundException | JsonProcessingException e) {
            return ResponseEntityFactory.produce(
                    InventoryManagementStringTools.getInternalServerErrorMsg(),
                    HttpStatus.INTERNAL_SERVER_ERROR, PATH + "/exporting"
            );
        }

        ItemDTO item = modelMapper.map(object, ItemDTO.class);
        if (!itemService.deleteItem(itemService.getItemId(item))) {
            return ResponseEntityFactory.produce(
                    InventoryManagementStringTools.getItemNotFoundMsg(),
                    HttpStatus.NOT_FOUND, PATH + "/exporting"
            );
        }

        PdfDTO pdf = new PdfDTO(Base64Encoder.encode(PdfFactory.produce(item, ImportExport.EXPORT)));

        return new ResponseEntity<>(pdf, HttpStatus.OK);
    }

    @Authorization(
            privileges = {
                    AccountPrivilege.ADMIN,
                    AccountPrivilege.EXPORTER,
                    AccountPrivilege.IMPORTER,
                    AccountPrivilege.IMPORTER_EXPORTER,
                    AccountPrivilege.MAINTENANCE
            }
    )
    @GetMapping(value = "/item/{itemId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getItem(@PathVariable("itemId") int itemId, @RequestParam String token) {
        if (!authorizationCheck.check(this.getClass(), token)) {
            return ResponseEntityFactory.produce(
                    InventoryManagementStringTools.getUnauthorizedMsg(),
                    HttpStatus.UNAUTHORIZED, PATH + "/item/" + itemId
            );
        }

        Optional<ItemDTO> item = itemService.fetchItem(itemId);
        return item.<ResponseEntity<?>>map(
                itemDTO ->
                        new ResponseEntity<>(itemDTO, HttpStatus.OK))
                .orElseGet(() ->
                        ResponseEntityFactory.produce(
                                InventoryManagementStringTools.getItemNotFoundMsg(),
                                HttpStatus.NOT_FOUND, PATH + "/item/" + itemId
                        )
                );
    }

    @Authorization(
            privileges = {
                    AccountPrivilege.ADMIN,
                    AccountPrivilege.EXPORTER,
                    AccountPrivilege.IMPORTER,
                    AccountPrivilege.IMPORTER_EXPORTER,
                    AccountPrivilege.MAINTENANCE
            }
    )
    @GetMapping(value = "/item", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllItems(@RequestParam String token) {
        if (!authorizationCheck.check(this.getClass(), token)) {
            return ResponseEntityFactory.produce(
                    InventoryManagementStringTools.getUnauthorizedMsg(),
                    HttpStatus.UNAUTHORIZED, PATH + "/item"
            );
        }

        List<ItemDTO> items = itemService.fetchAllItems();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @Authorization(
            privileges = {
                    AccountPrivilege.ADMIN,
                    AccountPrivilege.EXPORTER,
                    AccountPrivilege.IMPORTER,
                    AccountPrivilege.IMPORTER_EXPORTER,
                    AccountPrivilege.MAINTENANCE
            }
    )
    @GetMapping(value = "/name/{name}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getItemByName(@PathVariable("name") String name, @RequestParam String token) {
        if (!authorizationCheck.check(this.getClass(), token)) {
            return ResponseEntityFactory.produce(
                    InventoryManagementStringTools.getUnauthorizedMsg(),
                    HttpStatus.UNAUTHORIZED, PATH + "/name/" + name
            );
        }

        List<ItemDTO> items = itemService.fetchItemByName(name);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @Authorization(
            privileges = {
                    AccountPrivilege.ADMIN,
                    AccountPrivilege.EXPORTER,
                    AccountPrivilege.IMPORTER,
                    AccountPrivilege.IMPORTER_EXPORTER,
                    AccountPrivilege.MAINTENANCE
            }
    )
    @GetMapping(value = "/company/{company}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getItemByCompany(@PathVariable("company") String company, @RequestParam String token) {
        if (!authorizationCheck.check(this.getClass(), token)) {
            return ResponseEntityFactory.produce(
                    InventoryManagementStringTools.getUnauthorizedMsg(),
                    HttpStatus.UNAUTHORIZED, PATH + "/company/" + company
            );
        }

        List<ItemDTO> items = itemService.fetchItemByCompany(company);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @Authorization(
            privileges = {
                    AccountPrivilege.ADMIN,
                    AccountPrivilege.EXPORTER,
                    AccountPrivilege.IMPORTER,
                    AccountPrivilege.IMPORTER_EXPORTER,
                    AccountPrivilege.MAINTENANCE}
    )
    @GetMapping(value = "/location/{location}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getItemByLocation(@PathVariable("location") String location, @RequestParam String token) {
        if (!authorizationCheck.check(this.getClass(), token)) {
            return ResponseEntityFactory.produce(
                    InventoryManagementStringTools.getUnauthorizedMsg(),
                    HttpStatus.UNAUTHORIZED, PATH + "/company/" + location
            );
        }

        List<ItemDTO> items = itemService.fetchItemByLocation(location);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @Authorization(
            privileges = {
                    AccountPrivilege.ADMIN,
                    AccountPrivilege.EXPORTER,
                    AccountPrivilege.IMPORTER,
                    AccountPrivilege.IMPORTER_EXPORTER,
                    AccountPrivilege.MAINTENANCE
            }
    )
    @PostMapping(value = "/date", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getItemByDate(@RequestBody Object object, @RequestParam String token) {
        if (!authorizationCheck.check(this.getClass(), token)) {
            return ResponseEntityFactory.produce(
                    InventoryManagementStringTools.getUnauthorizedMsg(),
                    HttpStatus.UNAUTHORIZED, PATH + "/date"
            );
        }

        try {
            if (!JsonValidator.validate(JsonFactory.produce(object), DateQueryDTO.class)) {
                return ResponseEntityFactory.produce(
                        InventoryManagementStringTools.getBadRequestMsg(),
                        HttpStatus.BAD_REQUEST, PATH + "/date"
                );
            }
        } catch (SchemaNotFoundException | JsonProcessingException e) {
            return ResponseEntityFactory.produce(
                    InventoryManagementStringTools.getInternalServerErrorMsg(),
                    HttpStatus.INTERNAL_SERVER_ERROR, PATH + "/date"
            );
        }

        DateQueryDTO dateQuery = modelMapper.map(object, DateQueryDTO.class);
        List<ItemDTO> items = itemService.fetchItemByArrival(dateQuery.getStart(), dateQuery.getEnd());
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}
