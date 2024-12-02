package inventorymanagement.backend.util.auth.impl;

import inventorymanagement.backend.dto.TokenDTO;
import inventorymanagement.backend.service.AccountService;
import inventorymanagement.backend.util.auth.Authorization;
import inventorymanagement.backend.util.auth.AuthorizationCheck;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class AuthorizationCheckImpl implements AuthorizationCheck {

    @Autowired
    AccountService accountService;

    @Override
    public boolean check(Method method, String token) {

        if (!token.startsWith("Bearer ")) return false;

        Optional<TokenDTO> t = accountService.getToken(token.replace("Bearer ", ""));
        return t.isPresent()
                && accountService.accountExists(t.get().getUsername())
                && Arrays.stream(method.getAnnotation(Authorization.class).privileges()).toList()
                .contains(accountService.fetchAccountByUsername(t.get().getUsername()).get().getPrivilege());
    }
}
