package inventorymanagement.backend.util.encryption;

import inventorymanagement.backend.dto.AccountDTO;

public class CheckPassAgainst {
    public static boolean check(AccountDTO account, String hash) {
        return account.getPassword().equals(EncryptionFactory.encrypt(hash));
    }
}
