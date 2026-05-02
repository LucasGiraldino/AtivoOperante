package unoeste.fipp.ativooperante_be.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unoeste.fipp.ativooperante_be.entity.Denuncia;
import unoeste.fipp.ativooperante_be.entity.Usuario;
import unoeste.fipp.ativooperante_be.repository.DenunciaRepository;
import unoeste.fipp.ativooperante_be.repository.UsuarioRepository;
import unoeste.fipp.ativooperante_be.util.JWTTokenProvider;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuRepo;

    @Autowired
    private DenunciaRepository denunciaRepo;

    public List<Usuario> getAllUsuarios() {
        return usuRepo.findAll();
    }

    public Usuario getUsuarioId(Long id) {
        return usuRepo.findById(id).orElse(null);
    }

    public Usuario autenticar(String email, Long senha) {
        Usuario usuario = usuRepo.findByEmail(email);
        if (usuario != null && usuario.getSenha().equals(senha)) {
            return usuario;
        }
        return null;
    }

    public String gerarToken(Usuario usuario) {
        return JWTTokenProvider.getToken(usuario.getEmail(), String.valueOf(usuario.getNivel()));
    }

    public Usuario getUsuarioByEmail(String email) {
        return usuRepo.findByEmail(email);
    }

    public Usuario salvarUsuario(Usuario usuario) {
        return usuRepo.save(usuario);
    }

    public boolean existePorEmail(String email) {
        return usuRepo.existsByEmail(email);
    }

    public boolean existePorCpf(Long cpf) {
        return usuRepo.existsByCpf(cpf);
    }

    public boolean deletarUsuario(Usuario elemento) {
        try {
            Usuario usuario = usuRepo.findById(elemento.getId()).orElse(null);
            if (usuario != null) {
                usuRepo.delete(usuario);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public Usuario atualizarUsuario(Usuario novo) {
        Usuario usu = null;
        try {
            usu = usuRepo.findById(novo.getId()).orElse(null);
            if (usu != null) {
                usu.setCpf(novo.getCpf());
                usu.setEmail(novo.getEmail());
                usu.setNivel(novo.getNivel());
                usu.setSenha(novo.getSenha());
            }
        } catch (Exception e) {
            return usu;
        }
        return usu;
    }

    public List<Denuncia> getAllDenunciasByUsuario(Long usuarioId) {
        Usuario usuario = usuRepo.findById(usuarioId).orElse(null);
        if (usuario != null) {
            return denunciaRepo.findAllByUsuario(usuario);
        }
        return List.of();
    }
}
