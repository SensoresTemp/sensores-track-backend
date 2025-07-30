package sensores.track.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sensores.track.backend.mapper.UsuarioMapper;
import sensores.track.backend.model.Usuario;
import sensores.track.backend.model.Conta;
import sensores.track.backend.model.dto.LoginRequestDTO;
import sensores.track.backend.model.dto.LoginResponseDTO;
import sensores.track.backend.model.dto.UsuarioRequestDTO;
import sensores.track.backend.model.dto.UsuarioResponseDTO;
import sensores.track.backend.repository.ContaRepository;
import sensores.track.backend.repository.UsuarioRepository;

import java.util.List;

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
    public ResponseEntity<?> cadastrar(@Valid @RequestBody UsuarioRequestDTO dto) {
        return contaRepo.findById(dto.getIdConta())
                .<ResponseEntity<?>>map(conta -> {
                    Usuario usuario = usuarioMapper.toEntity(dto, conta);
                    Usuario salvo = usuarioRepo.save(usuario);
                    return ResponseEntity.ok(usuarioMapper.toResponse(salvo));
                })
                .orElseGet(() -> ResponseEntity.badRequest().body("Conta não encontrada"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                       @Valid @RequestBody UsuarioRequestDTO dto) {
        return usuarioRepo.findById(id)
                .<ResponseEntity<?>>map(usuarioExistente -> {
                    Conta conta = contaRepo.findById(dto.getIdConta())
                            .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

                    usuarioExistente.setEmail(dto.getEmail());
                    usuarioExistente.setSenha(dto.getSenha());
                    usuarioExistente.setPerfil(dto.getPerfil());
                    usuarioExistente.setConta(conta);

                    return ResponseEntity.ok(usuarioMapper.toResponse(usuarioRepo.save(usuarioExistente)));
                })
                .orElseGet(() -> ResponseEntity.status(404).body("Usuário não encontrado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        return usuarioRepo.findById(id)
                .<ResponseEntity<?>>map(usuario -> {
                    usuarioRepo.delete(usuario);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.status(404).body("Usuário não encontrado"));
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO dto) {

        return usuarioRepo.findByEmailAndSenha(dto.email(), dto.senha())
                .<ResponseEntity<?>>map(u -> ResponseEntity.ok(
                        new LoginResponseDTO(u.getId(), u.getConta().getId(), u.getPerfil().getDescricao())
                ))
                .orElseGet(() -> ResponseEntity.status(401)
                        .body("Usuário ou senha inválidos"));
    }
}
