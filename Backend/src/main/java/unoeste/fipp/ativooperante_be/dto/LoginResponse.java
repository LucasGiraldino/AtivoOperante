package unoeste.fipp.ativooperante_be.dto;

public class LoginResponse {
    private String token;
    private String email;
    private Long nivel;
    private Long usuarioId;

    public LoginResponse() {
    }

    public LoginResponse(String token, String email, Long nivel, Long usuarioId) {
        this.token = token;
        this.email = email;
        this.nivel = nivel;
        this.usuarioId = usuarioId;
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

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}

    public LoginResponse(String token, String email, Long nivel, Long usuarioId) {
        this.token = token;
        this.email = email;
        this.nivel = nivel;
        this.usuarioId = usuarioId;
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

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
