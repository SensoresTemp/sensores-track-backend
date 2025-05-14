package sensores.track.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sensores.track.backend.mapper.TipoSensorMapper;
import sensores.track.backend.model.TipoSensor;
import sensores.track.backend.model.dto.TipoSensorDTO;
import sensores.track.backend.repository.TipoSensorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-sensor")
@RequiredArgsConstructor
public class TipoSensorController {

    private final TipoSensorRepository tipoSensorRepo;
    private final TipoSensorMapper mapper;

    @PostMapping
    public ResponseEntity<TipoSensorDTO> criar(@Valid @RequestBody TipoSensorDTO dto) {
        TipoSensor tipoSensor = mapper.toEntity(dto);
        TipoSensor salvo = tipoSensorRepo.save(tipoSensor);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @GetMapping
    public List<TipoSensorDTO> listarTodos() {
        return tipoSensorRepo.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoSensorDTO> buscarPorId(@PathVariable Long id) {
        return tipoSensorRepo.findById(id)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoSensorDTO> atualizar(@PathVariable Long id, @Valid @RequestBody TipoSensorDTO dto) {
        return tipoSensorRepo.findById(id)
                .map(existing -> {
                    TipoSensor atualizado = mapper.toEntity(dto);
                    atualizado.setId(id);
                    return ResponseEntity.ok(mapper.toDTO(tipoSensorRepo.save(atualizado)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!tipoSensorRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        tipoSensorRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

