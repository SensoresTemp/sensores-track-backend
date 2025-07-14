package sensores.track.backend.mapper;

import org.springframework.stereotype.Component;
import sensores.track.backend.model.Conta;
import sensores.track.backend.model.Usuario;
import sensores.track.backend.model.dto.UsuarioResponseDTO;
import sensores.track.backend.model.dto.UsuarioRequestDTO;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequestDTO dto, Conta conta) {
        Usuario user = new Usuario();
        user.setEmail(dto.getEmail());
        user.setSenha(dto.getSenha()); // ideal criptografar
        user.setPerfil(dto.getPerfil());
        user.setConta(conta);
        return user;
    }

    public UsuarioResponseDTO toResponse(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setPerfil(usuario.getPerfil());
        dto.setIdConta(usuario.getConta().getId());
        return dto;
    }
}
