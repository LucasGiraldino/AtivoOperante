package unoeste.fipp.ativooperante_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unoeste.fipp.ativooperante_be.entity.Tipo;

public interface TipoRepository extends JpaRepository<Tipo, Long> {
}
