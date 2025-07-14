package sensores.track.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sensores.track.backend.model.LeituraSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LeituraSensorRepository extends JpaRepository<LeituraSensor, Long> {

    List<LeituraSensor> findBySensorIdAndConta_Id(Long idSensor, Long idConta);

    @Query("""
           SELECT l FROM LeituraSensor l 
           WHERE l.sensor.tipoSensor.tipo = :tipo 
             AND l.conta.id = :idConta
           """)
    List<LeituraSensor> findByTipoSensor(
            @Param("tipo") String tipo,
            @Param("idConta") Long idConta
    );

    @Query("""
           SELECT l FROM LeituraSensor l 
           WHERE l.sensor.tipoSensor.tipo = :tipo 
             AND l.dataHora BETWEEN :inicio AND :fim 
             AND l.conta.id = :idConta
           """)
    List<LeituraSensor> findByTipoSensorAndData(
            @Param("tipo") String tipo,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("idConta") Long idConta
    );

    @Query("""
           SELECT l FROM LeituraSensor l 
           WHERE l.sensor.tipoSensor.tipo = :tipo 
             AND l.dataHora BETWEEN :inicio AND :fim 
             AND l.conta.id = :idConta
           """)
    Page<LeituraSensor> findByTipoSensorAndDataPaginado(
            @Param("tipo") String tipo,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("idConta") Long idConta,
            Pageable pageable
    );

    List<LeituraSensor> findByDataHoraBetweenAndConta_Id(
            LocalDateTime inicio,
            LocalDateTime fim,
            Long idConta
    );

    LeituraSensor findTop1BySensorIdAndConta_IdOrderByIdDesc(Long idSensor, Long idConta);

}
