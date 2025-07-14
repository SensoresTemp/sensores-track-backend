package sensores.track.backend.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import sensores.track.backend.model.enums.PerfilUsuario;

@Data
public class UsuarioRequestDTO {

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    @NotNull(message = "O perfil é obrigatório")
    private PerfilUsuario perfil;

    private Long idConta = 1L; // default para multi-tenant

}
