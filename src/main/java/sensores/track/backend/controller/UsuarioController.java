package sensores.track.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sensores.track.backend.mapper.SensorMapper;
import sensores.track.backend.mapper.UsuarioMapper;
import sensores.track.backend.model.Usuario;
import sensores.track.backend.model.Conta;
import sensores.track.backend.model.dto.UsuarioRequestDTO;
import sensores.track.backend.model.dto.UsuarioResponseDTO;
import sensores.track.backend.repository.ContaRepository;
import sensores.track.backend.repository.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepo;
    private final ContaRepository contaRepo;
    private final UsuarioMapper usuarioMapper;

    /**
     * Cadastra um novo usuário.
     */
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Valid @RequestBody UsuarioRequestDTO dto) {
        Conta conta = contaRepo.findById(dto.getIdConta())
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        Usuario usuario = usuarioMapper.toEntity(dto, conta);
        Usuario salvo = usuarioRepo.save(usuario);

        return ResponseEntity.ok(usuarioMapper.toResponse(salvo));
    }

    /**
     * Lista todos os usuários de uma conta específica.
     */
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarPorConta(
            @RequestParam(name = "idConta", required = false) Long idConta) {

        if (idConta == null) idConta = 1L;

        List<Usuario> usuarios = usuarioRepo.findAllByConta_Id(idConta);
        List<UsuarioResponseDTO> dtos = usuarios.stream()
                .map(usuarioMapper::toResponse)
                .toList();

        return ResponseEntity.ok(dtos);
    }
}
