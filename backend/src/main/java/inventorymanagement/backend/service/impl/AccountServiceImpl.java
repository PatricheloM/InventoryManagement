package inventorymanagement.backend.service.impl;

import inventorymanagement.backend.dto.AccountDTO;
import inventorymanagement.backend.dto.TokenDTO;
import inventorymanagement.backend.model.Account;
import inventorymanagement.backend.model.Token;
import inventorymanagement.backend.repository.AccountRepository;
import inventorymanagement.backend.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AccountRepository accountRepository;


    @Override
    public boolean saveAccount(AccountDTO account) {
        return accountRepository.saveAccount(modelMapper.map(account, Account.class));
    }

    @Override
    public Optional<AccountDTO> fetchAccountByUsername(String username) {
        Optional<Account> account = accountRepository.fetchAccountByUsername(username);
        if (account.isPresent()) {
            return Optional.of(modelMapper.map(account, AccountDTO.class));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<AccountDTO> fetchAllAccounts() {
        return accountRepository.fetchAllAccounts().stream()
                .map(account -> modelMapper.map(account, AccountDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<AccountDTO> fetchAccountsByCompany(String companyName) {
        return accountRepository.fetchAccountsByCompany(companyName).stream()
                .map(account -> modelMapper.map(account, AccountDTO.class)).collect(Collectors.toList());
    }

    @Override
    public boolean changePassword(String username, String newPassword) {
        return accountRepository.changePassword(username, newPassword);
    }

    @Override
    public boolean deleteAccount(String username) {
        return accountRepository.deleteAccount(username);
    }

    @Override
    public boolean accountExists(String username) {
        return accountRepository.accountExists(username);
    }

    @Override
    public boolean saveToken(TokenDTO token) {
        return accountRepository.saveToken(modelMapper.map(token, Token.class));
    }

    @Override
    public boolean deleteToken(String token) {
        return accountRepository.deleteToken(token);
    }

    @Override
    public Optional<TokenDTO> getToken(String token) {
        Optional<Token> t = accountRepository.getToken(token);
        if (t.isPresent()) {
            return Optional.of(modelMapper.map(t, TokenDTO.class));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<TokenDTO> getTokens() {
        return accountRepository.getTokens().stream()
                .map(token -> modelMapper.map(token, TokenDTO.class)).collect(Collectors.toList());
    }
}
