package sensores.track.backend.mapper;

import sensores.track.backend.model.TipoSensor;
import sensores.track.backend.model.dto.TipoSensorDTO;
import org.springframework.stereotype.Component;

@Component
public class TipoSensorMapper {

    public TipoSensor toEntity(TipoSensorDTO dto) {
        TipoSensor tipoSensor = new TipoSensor();
        tipoSensor.setTipo(dto.getTipo());
        tipoSensor.setDescricao(dto.getDescricao());
        return tipoSensor;
    }

    public TipoSensorDTO toDTO(TipoSensor entity) {
        TipoSensorDTO dto = new TipoSensorDTO();
        dto.setId(entity.getId());
        dto.setTipo(entity.getTipo());
        dto.setDescricao(entity.getDescricao());
        return dto;
    }
}
