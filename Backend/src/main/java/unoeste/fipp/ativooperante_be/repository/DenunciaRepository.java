package unoeste.fipp.ativooperante_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import unoeste.fipp.ativooperante_be.entity.Denuncia;
import unoeste.fipp.ativooperante_be.entity.Usuario;

import java.util.List;

public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {

    @Modifying
    @Query(value = "UPDATE denuncia SET fee_texto = ?2 WHERE den_id = ?1", nativeQuery = true)
    void addFeedBack(Long id, String texto);

    void deleteByUsuario(Usuario usuario);

    List<Denuncia> findAllByUsuario(Usuario usuario);
}
