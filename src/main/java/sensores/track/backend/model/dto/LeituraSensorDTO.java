package sensores.track.backend.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class LeituraSensorDTO {

    @NotNull(message = "O ID do sensor é obrigatório.")
    private Long idSensor;

    @NotNull(message = "O valor é obrigatório.")
    private BigDecimal valor;

    private LocalDateTime dataHora;

}
