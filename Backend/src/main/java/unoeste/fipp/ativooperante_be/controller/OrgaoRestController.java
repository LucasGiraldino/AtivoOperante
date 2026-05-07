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
import unoeste.fipp.ativooperante_be.entity.Orgaos;
import unoeste.fipp.ativooperante_be.service.OrgaosService;

import java.util.List;

@RestController
@RequestMapping("apis/orgaos")
@Tag(name = "Órgãos", description = "CRUD de órgãos competentes")
@SecurityRequirement(name = "bearerAuth")
public class OrgaoRestController {

    @Autowired
    private OrgaosService orgaosService;

    @GetMapping
    @Operation(summary = "Listar órgãos", description = "Retorna todos os órgãos competentes cadastrados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de órgãos"),
        @ApiResponse(responseCode = "400", description = "Nenhum órgão encontrado")
    })
    public ResponseEntity<Object> getOrgaos() {
        List<Orgaos> orgaosList = orgaosService.getAllOrgaos();
        if (!orgaosList.isEmpty())
            return ResponseEntity.ok(orgaosList);
        else
            return ResponseEntity.badRequest().body(new Erro("Nenhum Orgaos cadastrado"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar órgão por ID", description = "Retorna um órgão pelo ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Órgão encontrado"),
        @ApiResponse(responseCode = "400", description = "Órgão não encontrado")
    })
    public ResponseEntity<Object> getOrgaosId(@PathVariable Long id) {
        Orgaos aux = orgaosService.getOrgaoId(id);
        if (aux != null)
            return ResponseEntity.ok(aux);
        return ResponseEntity.badRequest().body(new Erro("Este id nao existe"));
    }

    @PostMapping
    @Operation(summary = "Criar órgão", description = "Registra um novo órgão competente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Órgão criado"),
        @ApiResponse(responseCode = "400", description = "Erro ao criar")
    })
    public ResponseEntity<Object> save(@RequestBody Orgaos orgaos) {
        Orgaos orgaosAux = orgaosService.salvarOrgao(orgaos);
        if (orgaosAux != null)
            return ResponseEntity.ok(orgaosAux);
        return ResponseEntity.badRequest().body(new Erro("Erro ao gravar o Orgaos"));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir órgão", description = "Remove um órgão pelo ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Órgão excluído"),
        @ApiResponse(responseCode = "400", description = "Erro ao excluir")
    })
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Orgaos aux = orgaosService.getOrgaoId(id);
        if (aux != null) {
            boolean status = orgaosService.apagarOrgao(aux);
            if (status)
                return ResponseEntity.noContent().build();
            return ResponseEntity.badRequest().body(new Erro("Erro ao apagar Orgaos"));
        }
        return ResponseEntity.badRequest().body(new Erro("Erro ao encontrar Orgaos"));
    }

    @PutMapping
    @Operation(summary = "Atualizar órgão", description = "Atualiza os dados de um órgão existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Órgão atualizado"),
        @ApiResponse(responseCode = "400", description = "Erro ao atualizar")
    })
    public ResponseEntity<Object> update(@RequestBody Orgaos novo) {
        try {
            Orgaos alteradoOrgaos = orgaosService.salvarOrgao(novo);
            return ResponseEntity.ok(alteradoOrgaos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao alterar Orgaos"));
        }
    }
}
