package unoeste.fipp.ativooperante_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import unoeste.fipp.ativooperante_be.entity.Orgaos;

public interface OrgaosRepository extends JpaRepository<Orgaos, Long> {

    @Query(value = "INSERT INTO orgaos (org_nome) VALUES (?1)", nativeQuery = true)
    void addOrgao(String orgNome);
}
