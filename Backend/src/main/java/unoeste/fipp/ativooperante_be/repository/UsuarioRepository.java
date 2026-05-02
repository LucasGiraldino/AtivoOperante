package unoeste.fipp.ativooperante_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unoeste.fipp.ativooperante_be.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByCpf(Long cpf);
}
