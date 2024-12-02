package inventorymanagement.backend.service;

import inventorymanagement.backend.dto.AccountDTO;
import inventorymanagement.backend.dto.TokenDTO;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    boolean saveAccount(AccountDTO account);
    Optional<AccountDTO> fetchAccountByUsername(String username);
    List<AccountDTO> fetchAllAccounts();
    List<AccountDTO> fetchAccountsByCompany(String companyName);
    boolean changePassword(String username, String newPassword);
    boolean deleteAccount(String username);
    boolean accountExists(String username);
    void saveToken(TokenDTO token);
    void deleteToken(String token);
    Optional<TokenDTO> getToken(String token);
    List<TokenDTO> getTokens();
}
