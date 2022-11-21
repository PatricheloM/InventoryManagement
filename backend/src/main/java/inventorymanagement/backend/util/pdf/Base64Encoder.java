package inventorymanagement.backend.util.pdf;

import java.util.Base64;

public class Base64Encoder {
    public static String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
}
