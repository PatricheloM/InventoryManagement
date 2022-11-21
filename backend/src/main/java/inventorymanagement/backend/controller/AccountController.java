package inventorymanagement.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import inventorymanagement.backend.dto.AccountDTO;
import inventorymanagement.backend.dto.LoginDTO;
import inventorymanagement.backend.dto.TokenDTO;
import inventorymanagement.backend.service.AccountService;
import inventorymanagement.backend.util.InventoryManagementStringTools;
import inventorymanagement.backend.util.ResponseEntityFactory;
import inventorymanagement.backend.util.auth.Authorization;
import inventorymanagement.backend.util.auth.AuthorizationCheck;
import inventorymanagement.backend.util.auth.TokenFactory;
import inventorymanagement.backend.util.enums.AccountPrivilege;
import inventorymanagement.backend.util.exception.SchemaNotFoundException;
import inventorymanagement.backend.util.json.JsonFactory;
import inventorymanagement.backend.util.validator.JsonValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/account")
public class AccountController {

    private static final String PATH = "/api/account";

    @Autowired
    protected ModelMapper modelMapper;
    @Autowired
    AccountService accountService;

    @Autowired
    AuthorizationCheck authorizationCheck;

    @PostMapping(value = "/login", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Object> login(@RequestBody Object object) {
        try {
            if (JsonValidator.validate(JsonFactory.produce(object), LoginDTO.class)) {
                LoginDTO login = modelMapper.map(object, LoginDTO.class);
                Optional<AccountDTO> account = accountService.fetchAccountByUsername(login.getUsername().toLowerCase());
                if (account.isPresent() && account.get().getPassword().equals(login.getPassword())) {
                    String token = TokenFactory.generate();
                    TokenDTO t = new TokenDTO(token, account.get().getUsername());
                    accountService.saveToken(t);
                    return new ResponseEntity<>(t, HttpStatus.OK);
                } else {
                    return ResponseEntityFactory.produce(InventoryManagementStringTools.getForbiddenMsg(),
                            HttpStatus.FORBIDDEN, PATH + "/login");
                }
            }
            else
                return ResponseEntityFactory.produce(InventoryManagementStringTools.getBadRequestMsg(),
                        HttpStatus.BAD_REQUEST, PATH + "/login");
        }
        catch (SchemaNotFoundException | JsonProcessingException e) {
            return ResponseEntityFactory.produce(InventoryManagementStringTools.getInternalServerErrorMsg(),
                    HttpStatus.INTERNAL_SERVER_ERROR, PATH + "/login");
        }
    }

    @Authorization(privileges = { AccountPrivilege.ADMIN,
            AccountPrivilege.EXPORTER, AccountPrivilege.IMPORTER,
            AccountPrivilege.IMPORTER_EXPORTER, AccountPrivilege.MAINTENANCE})
    @GetMapping(value = "/logout", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> logout(@RequestParam String token) {
        if (authorizationCheck.check(new Object(){}.getClass().getEnclosingMethod(), token)) {
            accountService.deleteToken(token);
            return ResponseEntityFactory.produce(InventoryManagementStringTools.getLoggedOutMsg(),
                    HttpStatus.OK, PATH + "/logout");
        } else {
            return ResponseEntityFactory.produce(InventoryManagementStringTools.getUnauthorizedMsg(),
                    HttpStatus.UNAUTHORIZED, PATH + "/logout");
        }
    }

    @Authorization(privileges = { AccountPrivilege.ADMIN, AccountPrivilege.MAINTENANCE })
    @PostMapping(value = "/register", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> register(@RequestBody Object object, @RequestParam String token) {
        if (authorizationCheck.check(new Object(){}.getClass().getEnclosingMethod(), token)) {
            try {
                if (JsonValidator.validate(JsonFactory.produce(object), AccountDTO.class)) {
                    AccountDTO account = modelMapper.map(object, AccountDTO.class);
                    account.setUsername(account.getUsername().toLowerCase());
                    if (accountService.saveAccount(account)) {
                        return ResponseEntityFactory.produce(InventoryManagementStringTools.getSuccessfulRegistrationMsg(),
                                HttpStatus.OK, PATH + "/register");
                    } else {
                        return ResponseEntityFactory.produce(InventoryManagementStringTools.getUsernameAlreadyExistsMsg(),
                                HttpStatus.CONFLICT, PATH + "/register");
                    }
                }
                else
                    return ResponseEntityFactory.produce(InventoryManagementStringTools.getBadRequestMsg(),
                            HttpStatus.BAD_REQUEST, PATH + "/register");
            } catch (SchemaNotFoundException | JsonProcessingException e) {
                return ResponseEntityFactory.produce(InventoryManagementStringTools.getInternalServerErrorMsg(),
                        HttpStatus.INTERNAL_SERVER_ERROR, PATH + "/register");
            }
        } else {
            return ResponseEntityFactory.produce(InventoryManagementStringTools.getUnauthorizedMsg(),
                    HttpStatus.UNAUTHORIZED, PATH + "/register");
        }
    }

    @Authorization(privileges = { AccountPrivilege.ADMIN })
    @PatchMapping(value = "/{username}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> changePassword(@PathVariable("username")String username, @RequestParam String newPassword, @RequestParam String token) {
        if (authorizationCheck.check(new Object(){}.getClass().getEnclosingMethod(), token) ) {
            if (accountService.changePassword(username.toLowerCase(), newPassword)) {
                return ResponseEntityFactory.produce(InventoryManagementStringTools.getSuccessfulPasswordChangeMsg(),
                        HttpStatus.OK, PATH + "/" + username);
            } else {
                return ResponseEntityFactory.produce(InventoryManagementStringTools.getUserDoesNotExistMsg(),
                        HttpStatus.NOT_FOUND, PATH + "/" + username);
            }
        } else if (accountService.getToken(token).isPresent() && username.equals(accountService.getToken(token).get().getUsername())) {
            accountService.changePassword(username, newPassword);
            return ResponseEntityFactory.produce(InventoryManagementStringTools.getSuccessfulPasswordChangeMsg(),
                    HttpStatus.OK, PATH + "/" + username);
        } else {
            return ResponseEntityFactory.produce(InventoryManagementStringTools.getUnauthorizedMsg(),
                    HttpStatus.UNAUTHORIZED, PATH + "/" + username);
        }
    }

    @Authorization(privileges = { AccountPrivilege.ADMIN })
    @DeleteMapping(value = "/{username}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteUser(@PathVariable("username") String username, @RequestParam String token) {
        if (authorizationCheck.check(new Object(){}.getClass().getEnclosingMethod(), token) ) {
            if (accountService.deleteAccount(username.toLowerCase())) {
                for (TokenDTO t : accountService.getTokens()) {
                    if (username.equals(t.getUsername())) {
                        accountService.deleteToken(t.getToken());
                    }
                }
                return ResponseEntityFactory.produce(InventoryManagementStringTools.getUserDeletedMsg(),
                        HttpStatus.OK, PATH + "/" + username);
            } else {
                return ResponseEntityFactory.produce(InventoryManagementStringTools.getUserDoesNotExistMsg(),
                        HttpStatus.NOT_FOUND, PATH + "/" + username);
            }
        } else {
            return ResponseEntityFactory.produce(InventoryManagementStringTools.getUnauthorizedMsg(),
                    HttpStatus.UNAUTHORIZED, PATH + "/" + username);
        }
    }

    @GetMapping(value = "/token/{token}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> checkToken(@PathVariable("token") String token) {
        Optional<TokenDTO> t = accountService.getToken(token);
        if (t.isPresent()) {
            Optional<AccountDTO> account = accountService.fetchAccountByUsername(t.get().getUsername());
            return account.<ResponseEntity<Object>>map(accountDTO -> new ResponseEntity<>(accountDTO, HttpStatus.OK))
                    .orElseGet(() -> ResponseEntityFactory.produce(InventoryManagementStringTools.getUserDoesNotExistMsg(),
                    HttpStatus.NOT_FOUND, PATH + "/token/" + token));
        } else {
            return ResponseEntityFactory.produce(InventoryManagementStringTools.getTokenDoesNotExistMsg(),
                    HttpStatus.NOT_FOUND, PATH + "/token/" + token);
        }
    }

    @Authorization(privileges = { AccountPrivilege.ADMIN, AccountPrivilege.MAINTENANCE })
    @GetMapping(value = "/{username}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUser(@PathVariable("username") String username, @RequestParam String token) {
        if (authorizationCheck.check(new Object(){}.getClass().getEnclosingMethod(), token) ) {
            Optional<AccountDTO> account = accountService.fetchAccountByUsername(username.toLowerCase());
            return account.<ResponseEntity<Object>>map(accountDTO -> new ResponseEntity<>(accountDTO, HttpStatus.OK))
                    .orElseGet(() -> ResponseEntityFactory.produce(InventoryManagementStringTools.getUserDoesNotExistMsg(),
                            HttpStatus.NOT_FOUND, PATH + "/" + username));

        } else {
            return ResponseEntityFactory.produce(InventoryManagementStringTools.getUnauthorizedMsg(),
                    HttpStatus.UNAUTHORIZED, PATH + "/" + username);
        }
    }
}
