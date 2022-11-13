package inventorymanagement.backend.util.auth;

import inventorymanagement.backend.util.enums.AccountPrivilege;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Authorization {
    AccountPrivilege[] privileges();
}
