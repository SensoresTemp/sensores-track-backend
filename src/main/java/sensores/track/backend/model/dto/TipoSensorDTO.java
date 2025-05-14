package sensores.track.backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TipoSensorDTO {

    private Long id;

    @NotBlank(message = "O tipo é obrigatório.")
    private String tipo;

    private String descricao;
}
