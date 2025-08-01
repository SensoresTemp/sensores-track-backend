package sensores.track.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;
    private String descricao;
    private String unidade;
    private BigDecimal limitePpm;
    private BigDecimal limiteMinimoPpm;

    @ManyToOne
    @JoinColumn(name = "id_tipo_sensor", nullable = false)
    @JsonIgnore
    private TipoSensor tipoSensor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_conta", nullable = true)
    @JsonIgnore
    private Conta conta;

}
