package unoeste.fipp.ativooperante_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unoeste.fipp.ativooperante_be.entity.FeedBack;
import unoeste.fipp.ativooperante_be.entity.Denuncia;

import java.util.List;

public interface FeedBackRepository extends JpaRepository<FeedBack, Long> {

    List<FeedBack> findByDenuncia(Denuncia denuncia);
}
