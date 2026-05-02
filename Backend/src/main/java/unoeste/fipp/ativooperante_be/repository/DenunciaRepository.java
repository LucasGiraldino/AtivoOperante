package unoeste.fipp.ativooperante_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unoeste.fipp.ativooperante_be.entity.Denuncia;
import unoeste.fipp.ativooperante_be.entity.Usuario;

import java.util.List;

public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {

    void deleteByUsuario(Usuario usuario);

    List<Denuncia> findAllByUsuario(Usuario usuario);
}
