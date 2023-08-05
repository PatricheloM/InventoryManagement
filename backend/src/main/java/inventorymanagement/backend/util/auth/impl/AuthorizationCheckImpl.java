package inventorymanagement.backend.util.auth.impl;

import inventorymanagement.backend.controller.BaseController;
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
    public boolean check(Class<? extends BaseController> clazz, String token) {

        Optional<TokenDTO> t = accountService.getToken(token);

        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();

        return t.isPresent() && accountService.accountExists(t.get().getUsername())
                && Arrays.asList(Arrays.stream(clazz.getMethods())
                        .filter(name -> name.getName().equals(methodName))
                        .findFirst().get()
                        .getAnnotation(Authorization.class).privileges()
                )
                .contains(accountService.
                        fetchAccountByUsername(t.get().getUsername()).get().getPrivilege()
                );
    }
}
