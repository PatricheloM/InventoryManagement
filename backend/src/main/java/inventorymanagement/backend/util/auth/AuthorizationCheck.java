package inventorymanagement.backend.util.auth;

import java.lang.reflect.Method;

public interface AuthorizationCheck {
    boolean check(Method method, String token);
}
