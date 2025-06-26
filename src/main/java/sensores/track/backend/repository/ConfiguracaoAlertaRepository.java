package sensores.track.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sensores.track.backend.model.ConfiguracaoAlerta;

import java.util.Optional;

public interface ConfiguracaoAlertaRepository extends JpaRepository<ConfiguracaoAlerta, Long> {
    Optional<ConfiguracaoAlerta> findByEmail(String email);
}