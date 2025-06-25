package sensores.track.backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class SensorDTO {

    private Long id;

    @NotBlank(message = "O código do sensor é obrigatório.")
    private String codigo;

    @NotBlank(message = "A descrição é obrigatória.")
    private String descricao;

    @NotBlank(message = "A unidade é obrigatória.")
    private String unidade;

    private BigDecimal limitePpm;
    private BigDecimal limiteMinimoPpm;

    @NotNull(message = "O tipo do sensor é obrigatório.")
    private Long idTipoSensor;

}
