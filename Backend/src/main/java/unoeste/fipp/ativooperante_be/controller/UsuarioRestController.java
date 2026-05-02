package unoeste.fipp.ativooperante_be.controller;

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
public class UsuarioRestController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DenunciaService denunciaService;

    @GetMapping
    public ResponseEntity<Object> getUsuarios() {
        List<Usuario> lista = usuarioService.getAllUsuarios();
        if (lista != null)
            return ResponseEntity.ok(lista);
        return ResponseEntity.badRequest().body(new Erro("Problemas em listar usuario"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUsuarioId(@PathVariable Long id) {
        Usuario elemento = usuarioService.getUsuarioId(id);
        if (elemento != null)
            return ResponseEntity.ok(elemento);
        return ResponseEntity.badRequest().body(new Erro("Erro ao achar este Usuario"));
    }

    @PostMapping("/cadastrar")
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
    public ResponseEntity<Object> update(@RequestBody Usuario novo) {
        try {
            Usuario alteradoUsuario = usuarioService.atualizarUsuario(novo);
            return ResponseEntity.ok(alteradoUsuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao alterar Usuario"));
        }
    }

    @DeleteMapping("/{id}")
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
