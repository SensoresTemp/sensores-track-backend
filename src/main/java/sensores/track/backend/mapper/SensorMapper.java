package sensores.track.backend.mapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import sensores.track.backend.model.Conta;
import sensores.track.backend.model.Sensor;
import sensores.track.backend.model.TipoSensor;
import sensores.track.backend.model.dto.SensorDTO;
import sensores.track.backend.repository.ContaRepository;
import sensores.track.backend.repository.TipoSensorRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SensorMapper {

    private final TipoSensorRepository tipoSensorRepo;
    private final ContaRepository contaRepo;

    public Sensor toEntity(SensorDTO dto) {
        TipoSensor tipoSensor = tipoSensorRepo.findById(dto.getIdTipoSensor())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de sensor não encontrado"));

        Conta conta = contaRepo.findById(dto.getIdConta())
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        Sensor sensor = new Sensor();
        sensor.setCodigo(dto.getCodigo());
        sensor.setDescricao(dto.getDescricao());
        sensor.setUnidade(dto.getUnidade());
        sensor.setLimitePpm(dto.getLimitePpm());
        sensor.setTipoSensor(tipoSensor);
        sensor.setLimiteMinimoPpm(dto.getLimiteMinimoPpm());
        sensor.setConta(conta);

        return sensor;
    }

    public SensorDTO toDTO(Sensor entity) {
        SensorDTO dto = new SensorDTO();
        dto.setId(entity.getId());
        dto.setCodigo(entity.getCodigo());
        dto.setDescricao(entity.getDescricao());
        dto.setUnidade(entity.getUnidade());
        dto.setLimitePpm(entity.getLimitePpm());
        dto.setIdTipoSensor(entity.getTipoSensor().getId());
        dto.setLimiteMinimoPpm(entity.getLimiteMinimoPpm());
        dto.setIdConta(entity.getConta().getId());
        return dto;
    }
}

