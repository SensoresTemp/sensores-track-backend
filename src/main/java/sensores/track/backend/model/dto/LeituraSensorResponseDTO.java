package sensores.track.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
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

    @JsonSetter(nulls = Nulls.SKIP)
    private Long idConta = 1L;
}
