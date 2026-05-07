package unoeste.fipp.ativooperante_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unoeste.fipp.ativooperante_be.dto.Erro;
import unoeste.fipp.ativooperante_be.entity.Usuario;
import unoeste.fipp.ativooperante_be.service.DenunciaService;
import unoeste.fipp.ativooperante_be.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/apis/usuario")
@Tag(name = "Usuário", description = "CRUD de usuários e cadastro de cidadãos")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioRestController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DenunciaService denunciaService;

    @GetMapping
    @Operation(summary = "Listar usuários", description = "Retorna todos os usuários cadastrados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de usuários"),
        @ApiResponse(responseCode = "400", description = "Erro ao listar usuários")
    })
    public ResponseEntity<Object> getUsuarios() {
        List<Usuario> lista = usuarioService.getAllUsuarios();
        if (lista != null)
            return ResponseEntity.ok(lista);
        return ResponseEntity.badRequest().body(new Erro("Problemas em listar usuario"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário pelo ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
        @ApiResponse(responseCode = "400", description = "Usuário não encontrado")
    })
    public ResponseEntity<Object> getUsuarioId(@PathVariable Long id) {
        Usuario elemento = usuarioService.getUsuarioId(id);
        if (elemento != null)
            return ResponseEntity.ok(elemento);
        return ResponseEntity.badRequest().body(new Erro("Erro ao achar este Usuario"));
    }

    @PostMapping("/cadastrar")
    @Operation(summary = "Cadastrar cidadão", description = "Cria um novo usuário com perfil de cidadão (nivel=2)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro na validação ou email/CPF já cadastrado")
    })
    public ResponseEntity<Object> save(@RequestBody Usuario novo) {
        if (novo.getEmail() == null || novo.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body(new Erro("Email é obrigatório."));
        }
        if (novo.getCpf() == null) {
            return ResponseEntity.badRequest().body(new Erro("CPF é obrigatório."));
        }
        if (novo.getSenha() == null) {
            return ResponseEntity.badRequest().body(new Erro("Senha é obrigatória."));
        }
        if (novo.getCpf().toString().length() != 11) {
            return ResponseEntity.badRequest().body(new Erro("CPF deve ter 11 dígitos."));
        }
        if (usuarioService.existePorEmail(novo.getEmail())) {
            return ResponseEntity.badRequest().body(new Erro("Este E-mail já está em uso."));
        }
        if (usuarioService.existePorCpf(novo.getCpf())) {
            return ResponseEntity.badRequest().body(new Erro("Este CPF já está cadastrado."));
        }

        try {
            novo.setNivel(2L);
            Usuario novoUsuario = usuarioService.salvarUsuario(novo);
            return ResponseEntity.ok(novoUsuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao gravar o Usuario"));
        }
    }

    @PutMapping
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário atualizado"),
        @ApiResponse(responseCode = "400", description = "Erro ao atualizar")
    })
    public ResponseEntity<Object> update(@RequestBody Usuario novo) {
        try {
            Usuario alteradoUsuario = usuarioService.atualizarUsuario(novo);
            return ResponseEntity.ok(alteradoUsuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao alterar Usuario"));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir usuário", description = "Remove um usuário e suas denúncias")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuário excluído"),
        @ApiResponse(responseCode = "400", description = "Erro ao excluir")
    })
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Usuario aux = usuarioService.getUsuarioId(id);

        if (aux != null) {
            List<unoeste.fipp.ativooperante_be.entity.Denuncia> denuncias = usuarioService.getAllDenunciasByUsuario(id);
            if (!denuncias.isEmpty()) {
                denunciaService.deleteByUsuario(aux);
            }
            boolean status = usuarioService.deletarUsuario(aux);
            if (status)
                return ResponseEntity.noContent().build();
            return ResponseEntity.badRequest().body(new Erro("Erro ao apagar usuario"));
        }
        return ResponseEntity.badRequest().body(new Erro("Erro ao encontrar usuario"));
    }
}
