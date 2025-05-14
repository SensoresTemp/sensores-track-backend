package sensores.track.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sensores.track.backend.model.dto.SensorDTO;
import sensores.track.backend.mapper.SensorMapper;
import sensores.track.backend.model.Sensor;
import sensores.track.backend.repository.SensorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensores")
@RequiredArgsConstructor
public class SensorController {

    private final SensorRepository sensorRepo;
    private final SensorMapper mapper;

    @PostMapping
    public ResponseEntity<SensorDTO> criar(@Valid @RequestBody SensorDTO dto) {
        Sensor sensor = mapper.toEntity(dto);
        Sensor salvo = sensorRepo.save(sensor);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @GetMapping
    public List<SensorDTO> listarTodos() {
        return sensorRepo.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorDTO> buscarPorId(@PathVariable Long id) {
        return sensorRepo.findById(id)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SensorDTO> atualizar(@PathVariable Long id, @Valid @RequestBody SensorDTO dto) {
        return sensorRepo.findById(id)
                .map(existing -> {
                    Sensor sensorAtualizado = mapper.toEntity(dto);
                    sensorAtualizado.setId(id);
                    return ResponseEntity.ok(mapper.toDTO(sensorRepo.save(sensorAtualizado)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!sensorRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        sensorRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

