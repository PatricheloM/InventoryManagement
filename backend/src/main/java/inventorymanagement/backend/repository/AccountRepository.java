package inventorymanagement.backend.repository;

import inventorymanagement.backend.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    boolean saveAccount(Account account);
    Optional<Account> fetchAccountByUsername(String username);
    List<Account> fetchAllAccounts();
    boolean changePassword(String username, String newPassword);
    boolean deleteAccount(String username);
    boolean accountExists(String username);
}
