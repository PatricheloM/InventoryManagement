package inventorymanagement.backend.util.session;

import inventorymanagement.backend.util.enums.AccountPrivilege;

public interface Session {
    String getUsername();

    void setUsername(String username);

    AccountPrivilege getPrivilege();

    void setPrivilege(AccountPrivilege privilege);

    boolean getLoggedIn();

    void setLoggedIn(boolean loggedIn);
}
