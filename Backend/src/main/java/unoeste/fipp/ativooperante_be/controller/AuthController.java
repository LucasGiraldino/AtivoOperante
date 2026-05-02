package unoeste.fipp.ativooperante_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Autenticação", description = "Endpoints de login e geração de token JWT")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    @Operation(summary = "Realizar login", description = "Autentica o usuário e retorna token JWT")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Email ou senha inválidos")
    })
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
            return ResponseEntity.ok(new LoginResponse(token, usuario.getEmail(), usuario.getNivel(), usuario.getId()));
        }
        return ResponseEntity.badRequest().body(new Erro("Email ou senha inválidos"));
    }
}
