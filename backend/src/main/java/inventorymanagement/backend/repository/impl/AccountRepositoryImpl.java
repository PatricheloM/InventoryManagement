package inventorymanagement.backend.repository.impl;

import inventorymanagement.backend.model.Account;
import inventorymanagement.backend.model.Token;
import inventorymanagement.backend.repository.AccountRepository;
import inventorymanagement.backend.repository.RedisRepository;
import inventorymanagement.backend.util.InventoryManagementStringTools;
import inventorymanagement.backend.util.enums.AccountPrivilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

    @Autowired
    private RedisRepository redisRepository;

    @Override
    public boolean saveAccount(Account account) {
        if (!accountExists(account.getUsername())) {
            redisRepository.sadd(InventoryManagementStringTools.getAccountSetKey(), account.getUsername());
            redisRepository.hmset("ACCOUNT_" + account.getUsername(), Map.of(
                    "password", account.getPassword(),
                    "privilege", account.getPrivilege().toString(),
                    "companyName", account.getCompanyName(),
                    "companyEmail", account.getCompanyEmail()
            ));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<Account> fetchAccountByUsername(String username) {
        if (accountExists(username)) {
            Map<String, String> fetch = redisRepository.hgetall("ACCOUNT_" + username);
            Account account = new Account();
            account.setUsername(username);
            account.setPassword(fetch.get("password"));
            account.setPrivilege(AccountPrivilege.valueOf(fetch.get("privilege")));
            account.setCompanyName(fetch.get("companyName"));
            account.setCompanyEmail(fetch.get("companyEmail"));
            return Optional.of(account);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Account> fetchAllAccounts() {
        return redisRepository.smembers(InventoryManagementStringTools.getAccountSetKey())
                .stream().map(account -> fetchAccountByUsername(account).get()).collect(Collectors.toList());
    }

    @Override
    public List<Account> fetchAccountsByCompany(String companyName) {
        return redisRepository.smembers(InventoryManagementStringTools.getAccountSetKey())
                .stream().map(account -> fetchAccountByUsername(account).get())
                .filter(company -> company.getCompanyName().equals(companyName)).collect(Collectors.toList());
    }

    @Override
    public boolean changePassword(String username, String newPassword) {
        if (accountExists(username)) {
            redisRepository.hmset("ACCOUNT_" + username, Map.of("password", newPassword));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteAccount(String username) {
        if (accountExists(username)) {
            redisRepository.srem(InventoryManagementStringTools.getAccountSetKey(), username);
            redisRepository.del("ACCOUNT_" + username);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean accountExists(String username) {
        return redisRepository.sismember(InventoryManagementStringTools.getAccountSetKey(), username);
    }

    @Override
    public void saveToken(Token token) {
        redisRepository.hmset("TOKENS", new HashMap<>() {{ put(token.getToken(), token.getUsername()); }});
        redisRepository.hexpire("TOKENS", 86400, token.getToken());
    }

    @Override
    public void deleteToken(String token) {
        redisRepository.hdel("TOKENS", token);
    }

    @Override
    public Optional<Token> getToken(String token) {
        if (redisRepository.hexists("TOKENS", token)) {
            Token t = new Token();
            t.setToken(token);
            t.setUsername(redisRepository.hget("TOKENS", token));
            return Optional.of(t);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Token> getTokens() {
        List<Token> tokens = new ArrayList<>();
        for (var entry : redisRepository.hgetall("TOKENS").entrySet()) {
            Token t = new Token();
            t.setToken(entry.getKey());
            t.setUsername(entry.getValue());
            tokens.add(t);
        }
        return tokens.isEmpty() ? Collections.emptyList() : tokens;
    }
}
