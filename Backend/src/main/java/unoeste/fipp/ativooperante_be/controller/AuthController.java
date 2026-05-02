package unoeste.fipp.ativooperante_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unoeste.fipp.ativooperante_be.dto.Erro;
import unoeste.fipp.ativooperante_be.dto.LoginRequest;
import unoeste.fipp.ativooperante_be.dto.LoginResponse;
import unoeste.fipp.ativooperante_be.entity.Usuario;
import unoeste.fipp.ativooperante_be.service.UsuarioService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body(new Erro("Email é obrigatório"));
        }
        if (request.getSenha() == null) {
            return ResponseEntity.badRequest().body(new Erro("Senha é obrigatória"));
        }

        Usuario usuario = usuarioService.autenticar(request.getEmail(), request.getSenha());
        if (usuario != null) {
            String token = usuarioService.gerarToken(usuario);
            return ResponseEntity.ok(new LoginResponse(token, usuario.getEmail(), usuario.getNivel()));
        }
        return ResponseEntity.badRequest().body(new Erro("Email ou senha inválidos"));
    }
}
