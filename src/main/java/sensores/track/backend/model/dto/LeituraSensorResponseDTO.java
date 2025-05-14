package sensores.track.backend.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LeituraSensorResponseDTO {

    private Long id;
    private Long idSensor;
    private BigDecimal valor;
    private LocalDateTime dataHora;
    private String tipoSensor;
}
