package sensores.track.backend.model.dto;

import sensores.track.backend.model.enums.PerfilUsuario;

public record LoginResponseDTO(
        Long idUsuario,
        Long idConta,
        String perfil
) {}