package inventorymanagement.backend.dto;

import inventorymanagement.backend.util.enums.AccountPrivilege;

import java.util.Objects;

public class AccountDTO {
    private String username;
    private String password;
    private AccountPrivilege privilege;
    private String companyName;

    public AccountDTO() {
    }

    public AccountDTO(String username, String password, AccountPrivilege privilege, String companyName) {
        this.username = username;
        this.password = password;
        this.privilege = privilege;
        this.companyName = companyName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountPrivilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(AccountPrivilege privilege) {
        this.privilege = privilege;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountDTO that = (AccountDTO) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password) && privilege == that.privilege && Objects.equals(companyName, that.companyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, privilege, companyName);
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", privilege=" + privilege +
                ", company=" + companyName +
                '}';
    }
}
