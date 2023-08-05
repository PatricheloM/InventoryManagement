package inventorymanagement.backend.util.auth;

import inventorymanagement.backend.controller.BaseController;

import java.lang.reflect.Method;

public interface AuthorizationCheck {
    boolean check(Class<? extends BaseController> clazz, String token);
}
