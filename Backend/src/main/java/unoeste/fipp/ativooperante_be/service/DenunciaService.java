package unoeste.fipp.ativooperante_be.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unoeste.fipp.ativooperante_be.entity.Denuncia;
import unoeste.fipp.ativooperante_be.entity.Usuario;
import unoeste.fipp.ativooperante_be.repository.DenunciaRepository;
import unoeste.fipp.ativooperante_be.repository.UsuarioRepository;

import java.util.List;

@Service
public class DenunciaService {

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private UsuarioRepository usuRepo;

    @Transactional
    public void deleteByUsuario(Usuario usuario) {
        denunciaRepository.deleteByUsuario(usuario);
    }

    public List<Denuncia> getAll() {
        return denunciaRepository.findAll();
    }

    public Denuncia getDenunciaId(Long id) {
        return denunciaRepository.findById(id).orElse(null);
    }

    public Denuncia salvarDenuncia(Denuncia denuncia) {
        return denunciaRepository.save(denuncia);
    }

    public List<Denuncia> getAllByUsuario(Long id) {
        Usuario usuario = usuRepo.findById(id).orElse(null);
        if (usuario != null) {
            return denunciaRepository.findAllByUsuario(usuario);
        }
        return List.of();
    }

    public boolean deleteDenuncia(Denuncia denuncia) {
        try {
            Denuncia elemento = denunciaRepository.findById(denuncia.getId()).orElse(null);
            if (elemento != null) {
                denunciaRepository.delete(elemento);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public Denuncia atualizarDenuncia(Denuncia novo) {
        Denuncia status = null;
        try {
            Denuncia elemento = denunciaRepository.findById(novo.getId()).orElse(null);
            if (elemento != null) {
                elemento.setTexto(novo.getTexto());
                status = elemento;
                denunciaRepository.save(elemento);
            }
        } catch (Exception e) {
            return status;
        }
        return status;
    }
}
