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
import unoeste.fipp.ativooperante_be.entity.Tipo;
import unoeste.fipp.ativooperante_be.service.TipoService;

import java.util.List;

@RestController
@RequestMapping("apis/tipo")
@Tag(name = "Tipos", description = "CRUD de tipos de problema")
@SecurityRequirement(name = "bearerAuth")
public class TipoRestController {

    @Autowired
    private TipoService tipoService;

    @GetMapping
    @Operation(summary = "Listar tipos", description = "Retorna todos os tipos de problema cadastrados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de tipos"),
        @ApiResponse(responseCode = "400", description = "Nenhum tipo encontrado")
    })
    public ResponseEntity<Object> getTipos() {
        List<Tipo> tipoList = tipoService.getAll();
        if (!tipoList.isEmpty())
            return ResponseEntity.ok(tipoList);
        else
            return ResponseEntity.badRequest().body(new Erro("Nenhum tipo cadastrado"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tipo por ID", description = "Retorna um tipo pelo ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tipo encontrado"),
        @ApiResponse(responseCode = "400", description = "Tipo não encontrado")
    })
    public ResponseEntity<Object> getTipoId(@PathVariable Long id) {
        Tipo aux = tipoService.getTipoId(id);
        if (aux != null)
            return ResponseEntity.ok(aux);
        return ResponseEntity.badRequest().body(new Erro("Este id nao existe"));
    }

    @PostMapping
    @Operation(summary = "Criar tipo", description = "Registra um novo tipo de problema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tipo criado"),
        @ApiResponse(responseCode = "400", description = "Erro ao criar")
    })
    public ResponseEntity<Object> save(@RequestBody Tipo tipo) {
        Tipo tipoAux = tipoService.salvarTipo(tipo);
        if (tipoAux != null)
            return ResponseEntity.ok(tipoAux);
        return ResponseEntity.badRequest().body(new Erro("Erro ao gravar o tipo"));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir tipo", description = "Remove um tipo pelo ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Tipo excluído"),
        @ApiResponse(responseCode = "400", description = "Erro ao excluir")
    })
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Tipo aux = tipoService.getTipoId(id);
        if (aux != null) {
            boolean status = tipoService.deleteTipo(aux);
            if (status)
                return ResponseEntity.noContent().build();
            return ResponseEntity.badRequest().body(new Erro("Erro ao apagar tipo"));
        }
        return ResponseEntity.badRequest().body(new Erro("Erro ao encontrar tipo"));
    }

    @PutMapping
    @Operation(summary = "Atualizar tipo", description = "Atualiza os dados de um tipo existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tipo atualizado"),
        @ApiResponse(responseCode = "400", description = "Erro ao atualizar")
    })
    public ResponseEntity<Object> update(@RequestBody Tipo novo) {
        try {
            Tipo alteradoTipo = tipoService.atualizarTipo(novo);
            return ResponseEntity.ok(alteradoTipo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao alterar tipo"));
        }
    }
}
