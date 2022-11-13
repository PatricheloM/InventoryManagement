package inventorymanagement.backend.util.session.impl;

import inventorymanagement.backend.util.enums.AccountPrivilege;
import inventorymanagement.backend.util.session.Session;


public class SessionImpl implements Session {

    public SessionImpl() {
        username = null;
        privilege = null;
        loggedIn = false;
    }

    private String username;
    private AccountPrivilege privilege;
    private boolean loggedIn;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AccountPrivilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(AccountPrivilege privilege) {
        this.privilege = privilege;
    }

    public boolean getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
       this.loggedIn = loggedIn;
    }
}
