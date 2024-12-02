package inventorymanagement.backend.util.auth;

import java.security.SecureRandom;
import java.util.Base64;

public final class TokenFactory {

    private TokenFactory(){}

    public static String generate() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }
}
