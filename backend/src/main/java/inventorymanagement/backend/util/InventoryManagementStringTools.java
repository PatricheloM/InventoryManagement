package inventorymanagement.backend.util;

import inventorymanagement.backend.util.constants.RedisConstants;

public class InventoryManagementStringTools {
    public static String getAccountSetKey() {
        return RedisConstants.ACCOUNT_NAMES_KEY;
    }
}
