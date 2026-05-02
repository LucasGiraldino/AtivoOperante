package unoeste.fipp.ativooperante_be.controller;

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
public class FeedbackController {

    @Autowired
    private FeedBackService feedBackService;

    @Autowired
    private DenunciaService denunciaService;

    @GetMapping("/usuario/{id}")
    public ResponseEntity<Object> getByUsuario(@PathVariable Long id) {
        List<FeedBack> lista = feedBackService.getByUsuarioId(id);
        if (!lista.isEmpty()) {
            return ResponseEntity.ok(lista);
        }
        return ResponseEntity.ok(List.of());
    }

    @PostMapping
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
