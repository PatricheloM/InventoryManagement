package inventorymanagement.backend.service;

import inventorymanagement.backend.dto.AccountDTO;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    boolean saveAccount(AccountDTO account);
    Optional<AccountDTO> fetchAccountByUsername(String username);
    List<AccountDTO> fetchAllAccounts();
    boolean changePassword(String username, String newPassword);
    boolean deleteAccount(String username);
    boolean accountExists(String username);
}
