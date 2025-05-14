package sensores.track.backend.repository;

import sensores.track.backend.model.TipoSensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoSensorRepository extends JpaRepository<TipoSensor, Long> {}
