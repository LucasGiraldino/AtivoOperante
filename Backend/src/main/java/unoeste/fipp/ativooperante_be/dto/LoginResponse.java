package unoeste.fipp.ativooperante_be.dto;

public class LoginResponse {
    private String token;
    private String email;
    private Long nivel;

    public LoginResponse() {
    }

    public LoginResponse(String token, String email, Long nivel) {
        this.token = token;
        this.email = email;
        this.nivel = nivel;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getNivel() {
        return nivel;
    }

    public void setNivel(Long nivel) {
        this.nivel = nivel;
    }
}
