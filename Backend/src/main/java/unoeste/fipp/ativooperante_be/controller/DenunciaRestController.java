package unoeste.fipp.ativooperante_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unoeste.fipp.ativooperante_be.dto.Erro;
import unoeste.fipp.ativooperante_be.entity.Denuncia;
import unoeste.fipp.ativooperante_be.service.DenunciaService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("apis/denuncia")
@Tag(name = "Denúncia", description = "CRUD de denúncias e consulta por usuário")
public class DenunciaRestController {

    @Autowired
    private DenunciaService denunciaService;

    @GetMapping("/")
    @Operation(summary = "Listar todas denúncias", description = "Retorna todas as denúncias cadastradas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de denúncias"),
        @ApiResponse(responseCode = "400", description = "Nenhuma denúncia encontrada")
    })
    public ResponseEntity<Object> getDenuncias() {
        List<Denuncia> denunciaList = denunciaService.getAll();
        if (!denunciaList.isEmpty())
            return ResponseEntity.ok(denunciaList);
        return ResponseEntity.badRequest().body(new Erro("Nenhum Denuncia cadastrado"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar denúncia por ID", description = "Retorna uma denúncia pelo ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Denúncia encontrada"),
        @ApiResponse(responseCode = "400", description = "Denúncia não encontrada")
    })
    public ResponseEntity<Object> getDenunciaId(@PathVariable Long id) {
        Denuncia aux = denunciaService.getDenunciaId(id);
        if (aux != null)
            return ResponseEntity.ok(aux);
        return ResponseEntity.badRequest().body(new Erro("Este id nao existe"));
    }

    @PostMapping
    @Operation(summary = "Criar denúncia", description = "Registra uma nova denúncia")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Denúncia criada"),
        @ApiResponse(responseCode = "400", description = "Erro ao criar")
    })
    public ResponseEntity<Object> save(@RequestBody Denuncia denuncia) {
        Denuncia denunciaAux = denunciaService.salvarDenuncia(denuncia);
        if (denunciaAux != null)
            return ResponseEntity.ok(denunciaAux);
        return ResponseEntity.badRequest().body(new Erro("Erro ao gravar o Denuncia"));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir denúncia", description = "Remove uma denúncia pelo ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Denúncia excluída"),
        @ApiResponse(responseCode = "400", description = "Erro ao excluir")
    })
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Denuncia aux = denunciaService.getDenunciaId(id);
        if (aux != null) {
            boolean status = denunciaService.deleteDenuncia(aux);
            if (status)
                return ResponseEntity.noContent().build();
            return ResponseEntity.badRequest().body(new Erro("Erro ao apagar Denuncia"));
        }
        return ResponseEntity.badRequest().body(new Erro("Erro ao encontrar Denuncia"));
    }

    @PutMapping
    @Operation(summary = "Atualizar denúncia", description = "Atualiza os dados de uma denúncia")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Denúncia atualizada"),
        @ApiResponse(responseCode = "400", description = "Erro ao atualizar")
    })
    public ResponseEntity<Object> update(@RequestBody Denuncia novo) {
        try {
            Denuncia alteradoDenuncia = denunciaService.salvarDenuncia(novo);
            return ResponseEntity.ok(alteradoDenuncia);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao alterar Denuncia"));
        }
    }

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @PostMapping("/{id}/foto")
    @Operation(summary = "Upload de foto", description = "Adiciona uma foto a uma denúncia existente (max 5MB)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Foto enviada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao enviar foto")
    })
    public ResponseEntity<Object> uploadFoto(
            @PathVariable Long id,
            @RequestParam("foto") MultipartFile file) {
        Denuncia denuncia = denunciaService.getDenunciaId(id);
        if (denuncia == null) {
            return ResponseEntity.badRequest().body(new Erro("Denúncia não encontrada."));
        }
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(new Erro("Arquivo vazio."));
        }

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String extensao = "";
            String original = file.getOriginalFilename();
            if (original != null && original.contains(".")) {
                extensao = original.substring(original.lastIndexOf("."));
            }
            String nomeArquivo = UUID.randomUUID().toString() + extensao;
            Path destino = uploadPath.resolve(nomeArquivo);
            Files.write(destino, file.getBytes());

            denuncia.setFoto("/uploads/" + nomeArquivo);
            denunciaService.salvarDenuncia(denuncia);
            return ResponseEntity.ok(denuncia.getFoto());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao salvar arquivo: " + e.getMessage()));
        }
    }

    @GetMapping("/usuario/{id}")
    @Operation(summary = "Denúncias por usuário", description = "Lista todas as denúncias de um usuário específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de denúncias do usuário"),
        @ApiResponse(responseCode = "400", description = "Nenhuma denúncia para este usuário")
    })
    public ResponseEntity<Object> getAllByUsuario(@PathVariable Long id) {
        List<Denuncia> denunciaList = denunciaService.getAllByUsuario(id);
        if (!denunciaList.isEmpty())
            return ResponseEntity.ok(denunciaList);
        else
            return ResponseEntity.badRequest().body(new Erro("Nenhuma denuncia cadastrada para esse usuário"));
    }
}
