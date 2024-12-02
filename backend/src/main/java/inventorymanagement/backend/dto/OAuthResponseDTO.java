package inventorymanagement.backend.dto;

import java.util.Objects;

public class OAuthResponseDTO {
    private String access_token;
    private final String token_type = "Bearer";
    private final int expires_in = 86400;

    public OAuthResponseDTO(String access_token) {
        this.access_token = access_token;
    }

    public OAuthResponseDTO() {
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OAuthResponseDTO that = (OAuthResponseDTO) o;
        return expires_in == that.expires_in && Objects.equals(access_token, that.access_token) && Objects.equals(token_type, that.token_type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(access_token, token_type, expires_in);
    }

    @Override
    public String toString() {
        return "OAuthResponseDTO{" +
                "access_token='" + access_token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", expires_in=" + expires_in +
                '}';
    }
}
