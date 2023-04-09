package inventorymanagement.backend.dto;

import java.util.Objects;

public class TokenDTO {
    private String token;
    private String username;

    public TokenDTO() {
    }

    public TokenDTO(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenDTO tokenDTO = (TokenDTO) o;
        return Objects.equals(token, tokenDTO.token) && Objects.equals(username, tokenDTO.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, username);
    }

    @Override
    public String toString() {
        return "TokenDTO{" +
                "token='" + token + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
