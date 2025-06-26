package sensores.track.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sensores.track.backend.model.ConfiguracaoAlerta;
import sensores.track.backend.service.ConfiguracaoAlertaService;

import java.util.List;

@RestController
@RequestMapping("/api/configuracoes-alerta")
@RequiredArgsConstructor
public class ConfiguracaoAlertaController {

    private final ConfiguracaoAlertaService service;

    @PostMapping
    public ResponseEntity<ConfiguracaoAlerta> cadastrar(@RequestParam String email) {
        ConfiguracaoAlerta alerta = service.salvar(email);
        return ResponseEntity.ok(alerta);
    }

    @GetMapping
    public List<ConfiguracaoAlerta> listarTodos() {
        return service.listarTodos();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
