package inventorymanagement.backend.util.auth;

import java.util.UUID;

public class TokenFactory {
    public static String generate() {
        return UUID.randomUUID().toString();
    }
}
