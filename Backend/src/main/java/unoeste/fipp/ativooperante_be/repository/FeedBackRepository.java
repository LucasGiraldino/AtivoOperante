package unoeste.fipp.ativooperante_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import unoeste.fipp.ativooperante_be.entity.FeedBack;
import unoeste.fipp.ativooperante_be.entity.Denuncia;

import java.util.List;

public interface FeedBackRepository extends JpaRepository<FeedBack, Long> {

    List<FeedBack> findByDenuncia(Denuncia denuncia);

    @Query("SELECT f FROM FeedBack f WHERE f.denuncia.usuario.id = :usuarioId")
    List<FeedBack> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}
