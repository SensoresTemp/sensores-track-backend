package sensores.track.backend.mapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import sensores.track.backend.model.Conta;
import sensores.track.backend.model.TipoSensor;
import sensores.track.backend.model.dto.TipoSensorDTO;
import org.springframework.stereotype.Component;
import sensores.track.backend.repository.ContaRepository;

@Component
@RequiredArgsConstructor
public class TipoSensorMapper {

    private final ContaRepository contaRepo;

    public TipoSensor toEntity(TipoSensorDTO dto) {

        Conta conta = contaRepo.findById(dto.getIdConta())
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        TipoSensor tipoSensor = new TipoSensor();
        tipoSensor.setTipo(dto.getTipo());
        tipoSensor.setDescricao(dto.getDescricao());
        tipoSensor.setConta(conta);
        return tipoSensor;
    }

    public TipoSensorDTO toDTO(TipoSensor entity) {
        TipoSensorDTO dto = new TipoSensorDTO();
        dto.setId(entity.getId());
        dto.setTipo(entity.getTipo());
        dto.setDescricao(entity.getDescricao());
        dto.setIdConta(entity.getConta().getId());
        return dto;
    }
}
