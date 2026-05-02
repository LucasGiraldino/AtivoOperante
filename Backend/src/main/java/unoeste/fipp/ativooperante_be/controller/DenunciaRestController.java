package unoeste.fipp.ativooperante_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unoeste.fipp.ativooperante_be.dto.Erro;
import unoeste.fipp.ativooperante_be.entity.Denuncia;
import unoeste.fipp.ativooperante_be.service.DenunciaService;

import java.util.List;

@RestController
@RequestMapping("apis/denuncia")
public class DenunciaRestController {

    @Autowired
    private DenunciaService denunciaService;

    @GetMapping("/")
    public ResponseEntity<Object> getDenuncias() {
        List<Denuncia> denunciaList = denunciaService.getAll();
        if (!denunciaList.isEmpty())
            return ResponseEntity.ok(denunciaList);
        return ResponseEntity.badRequest().body(new Erro("Nenhum Denuncia cadastrado"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDenunciaId(@PathVariable Long id) {
        Denuncia aux = denunciaService.getDenunciaId(id);
        if (aux != null)
            return ResponseEntity.ok(aux);
        return ResponseEntity.badRequest().body(new Erro("Este id nao existe"));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Denuncia denuncia) {
        Denuncia denunciaAux = denunciaService.salvarDenuncia(denuncia);
        if (denunciaAux != null)
            return ResponseEntity.ok(denunciaAux);
        return ResponseEntity.badRequest().body(new Erro("Erro ao gravar o Denuncia"));
    }

    @DeleteMapping("{id}")
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
    public ResponseEntity<Object> update(@RequestBody Denuncia novo) {
        try {
            Denuncia alteradoDenuncia = denunciaService.salvarDenuncia(novo);
            return ResponseEntity.ok(alteradoDenuncia);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao alterar Denuncia"));
        }
    }

    @GetMapping("usuario/{id}")
    public ResponseEntity<Object> getAllByUsuario(@PathVariable Long id) {
        List<Denuncia> denunciaList = denunciaService.getAllByUsuario(id);
        if (!denunciaList.isEmpty())
            return ResponseEntity.ok(denunciaList);
        else
            return ResponseEntity.badRequest().body(new Erro("Nenhuma denuncia cadastrada para esse usuário"));
    }
}
