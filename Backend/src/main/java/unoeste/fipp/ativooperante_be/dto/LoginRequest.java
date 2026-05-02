package unoeste.fipp.ativooperante_be.dto;

public class LoginRequest {
    private String email;
    private Long senha;

    public LoginRequest() {
    }

    public LoginRequest(String email, Long senha) {
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getSenha() {
        return senha;
    }

    public void setSenha(Long senha) {
        this.senha = senha;
    }
}
