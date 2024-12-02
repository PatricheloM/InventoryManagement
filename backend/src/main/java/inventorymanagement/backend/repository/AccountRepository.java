package inventorymanagement.backend.repository;

import inventorymanagement.backend.model.Account;
import inventorymanagement.backend.model.Token;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    boolean saveAccount(Account account);
    Optional<Account> fetchAccountByUsername(String username);
    List<Account> fetchAllAccounts();
    List<Account> fetchAccountsByCompany(String companyName);
    boolean changePassword(String username, String newPassword);
    boolean deleteAccount(String username);
    boolean accountExists(String username);
    void saveToken(Token token);
    void deleteToken(String token);
    Optional<Token> getToken(String token);
    List<Token> getTokens();
}
