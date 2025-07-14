package sensores.track.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sensores.track.backend.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca por e-mail (útil em login ou validação)
    Optional<Usuario> findByEmail(String email);

    // Lista todos os usuários de uma conta específica
    List<Usuario> findAllByConta_Id(Long idConta);

    // Busca um usuário específico por ID e conta (padrão multi-tenant seguro)
    Optional<Usuario> findByIdAndConta_Id(Long id, Long idConta);

}
