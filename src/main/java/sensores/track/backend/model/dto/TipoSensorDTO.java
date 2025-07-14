package sensores.track.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TipoSensorDTO {

    private Long id;

    @NotBlank(message = "O tipo é obrigatório.")
    private String tipo;

    private String descricao;

    @JsonSetter(nulls = Nulls.SKIP)
    private Long idConta = 1L;
}
