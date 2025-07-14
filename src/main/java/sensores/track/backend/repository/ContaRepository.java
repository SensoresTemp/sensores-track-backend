package sensores.track.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sensores.track.backend.model.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {
}
