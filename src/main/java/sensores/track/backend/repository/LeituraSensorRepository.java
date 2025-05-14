package sensores.track.backend.repository;

import sensores.track.backend.model.LeituraSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LeituraSensorRepository extends JpaRepository<LeituraSensor, Long> {

    List<LeituraSensor> findBySensorId(Long idSensor);

    @Query("SELECT l FROM LeituraSensor l WHERE l.sensor.tipoSensor.tipo = :tipo")
    List<LeituraSensor> findByTipoSensor(@Param("tipo") String tipo);

    @Query("SELECT l FROM LeituraSensor l WHERE l.sensor.tipoSensor.tipo = :tipo AND l.dataHora BETWEEN :inicio AND :fim")
    List<LeituraSensor> findByTipoSensorAndData(@Param("tipo") String tipo, @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    List<LeituraSensor> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);

}
