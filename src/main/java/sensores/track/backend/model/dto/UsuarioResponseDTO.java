package sensores.track.backend.model.dto;

import lombok.Data;
import sensores.track.backend.model.enums.PerfilUsuario;

@Data
public class UsuarioResponseDTO {

    private Long id;
    private String email;
    private PerfilUsuario perfil;
    private Long idConta;
}