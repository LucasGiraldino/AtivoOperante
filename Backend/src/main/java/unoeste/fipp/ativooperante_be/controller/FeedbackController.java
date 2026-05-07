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
import unoeste.fipp.ativooperante_be.entity.Denuncia;
import unoeste.fipp.ativooperante_be.entity.FeedBack;
import unoeste.fipp.ativooperante_be.service.DenunciaService;
import unoeste.fipp.ativooperante_be.service.FeedBackService;

import java.util.List;

@RestController
@RequestMapping("apis/feedback")
@Tag(name = "Feedback", description = "CRUD de feedbacks em denúncias")
@SecurityRequirement(name = "bearerAuth")
public class FeedbackController {

    @Autowired
    private FeedBackService feedBackService;

    @Autowired
    private DenunciaService denunciaService;

    @GetMapping("/usuario/{id}")
    @Operation(summary = "Feedbacks por usuário", description = "Lista todos os feedbacks das denúncias de um usuário")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de feedbacks ou array vazio")
    })
    public ResponseEntity<Object> getByUsuario(@PathVariable Long id) {
        List<FeedBack> lista = feedBackService.getByUsuarioId(id);
        if (!lista.isEmpty()) {
            return ResponseEntity.ok(lista);
        }
        return ResponseEntity.ok(List.of());
    }

    @PostMapping
    @Operation(summary = "Criar feedback", description = "Registra um novo feedback em uma denúncia")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Feedback criado"),
        @ApiResponse(responseCode = "400", description = "Erro na validação ou denúncia não encontrada")
    })
    public ResponseEntity<Object> addFeedback(@RequestBody FeedBack feedback) {
        if (feedback.getFee_texto() == null || feedback.getFee_texto().isBlank()) {
            return ResponseEntity.badRequest().body(new Erro("Texto do feedback é obrigatório."));
        }
        if (feedback.getDenuncia() == null || feedback.getDenuncia().getId() == null) {
            return ResponseEntity.badRequest().body(new Erro("ID da denúncia é obrigatório."));
        }

        try {
            Denuncia denuncia = denunciaService.getDenunciaId(feedback.getDenuncia().getId());
            if (denuncia == null) {
                return ResponseEntity.badRequest().body(new Erro("Denúncia não encontrada."));
            }
            feedback.setDenuncia(denuncia);
            FeedBack salvo = feedBackService.salvarFeedBack(feedback);
            return ResponseEntity.ok(salvo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao adicionar feedback."));
        }
    }

    @PutMapping
    @Operation(summary = "Atualizar feedback", description = "Atualiza o texto de um feedback existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Feedback atualizado"),
        @ApiResponse(responseCode = "400", description = "Feedback não encontrado ou erro ao atualizar")
    })
    public ResponseEntity<Object> updateFeedback(@RequestBody FeedBack feedback) {
        if (feedback.getFee_id() == null) {
            return ResponseEntity.badRequest().body(new Erro("ID do feedback é obrigatório."));
        }
        try {
            FeedBack atualizado = feedBackService.atualizarFeedBack(feedback);
            if (atualizado != null) {
                return ResponseEntity.ok(atualizado);
            }
            return ResponseEntity.badRequest().body(new Erro("Feedback não encontrado."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao atualizar feedback."));
        }
    }
}
