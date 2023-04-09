package inventorymanagement.backend.util.auth;

import java.util.UUID;

public final class TokenFactory {

    private TokenFactory(){}

    public static String generate() {
        return UUID.randomUUID().toString();
    }
}
