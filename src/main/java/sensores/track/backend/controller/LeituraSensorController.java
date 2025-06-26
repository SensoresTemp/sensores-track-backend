package sensores.track.backend.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sensores.track.backend.model.LeituraSensor;
import sensores.track.backend.model.dto.LeituraSensorDTO;
import sensores.track.backend.model.Sensor;
import sensores.track.backend.model.dto.LeituraSensorResponseDTO;
import sensores.track.backend.repository.LeituraSensorRepository;
import sensores.track.backend.repository.SensorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.List;

@RestController
@RequestMapping("/api/leitura/sensor")
@RequiredArgsConstructor
public class LeituraSensorController {

    private final LeituraSensorRepository leituraRepo;
    private final SensorRepository sensorRepo;

    @PostMapping
    public ResponseEntity<Void> registrarLeitura(@Valid @RequestBody LeituraSensorDTO dto) {
        Sensor sensor = sensorRepo.findById(dto.getIdSensor())
                .orElseThrow(() -> new EntityNotFoundException("Sensor não encontrado"));

        ZonedDateTime dataHoraBrasil = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));

        LeituraSensor leitura = new LeituraSensor(null, sensor, dto.getValor(), dataHoraBrasil.toLocalDateTime());
        leituraRepo.save(leitura);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<LeituraSensorResponseDTO> listarTodas() {
        return leituraRepo.findAll().stream().map(this::toResponse).toList();
    }

    @GetMapping("/{idSensor}")
    public List<LeituraSensorResponseDTO> listarPorSensor(@PathVariable Long idSensor) {
        return leituraRepo.findBySensorId(idSensor).stream().map(this::toResponse).toList();
    }

    @GetMapping("/{idSensor}/ultima-leitura")
    public ResponseEntity<LeituraSensorResponseDTO> obterUltimaLeitura(@PathVariable Long idSensor) {
        LeituraSensor ultima = leituraRepo.findTop1BySensorIdOrderByIdDesc(idSensor);

        if (ultima == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toResponse(ultima));
    }


    @GetMapping("/por-tipo")
    public List<LeituraSensorResponseDTO> listarPorTipo(@RequestParam String tipo) {
        return leituraRepo.findByTipoSensor(tipo).stream().map(this::toResponse).toList();
    }

    @GetMapping("/por-tipo/intervalo")
    public List<LeituraSensorResponseDTO> listarPorTipoEData(@RequestParam String tipo,
                                                             @RequestParam LocalDateTime dataInicio,
                                                             @RequestParam LocalDateTime dataFim) {

        return leituraRepo.findByTipoSensorAndData(tipo, dataInicio, dataFim).stream().map(this::toResponse).toList();
    }

    @GetMapping("/por-tipo/intervalo/paginado")
    public ResponseEntity<Page<LeituraSensorResponseDTO>> listarPorTipoEDataPaginado(
            @RequestParam String tipo,
            @RequestParam LocalDateTime dataInicio,
            @RequestParam LocalDateTime dataFim,
            Pageable pageable) {

        Page<LeituraSensor> page = leituraRepo.findByTipoSensorAndDataPaginado(tipo, dataInicio, dataFim, pageable);

        Page<LeituraSensorResponseDTO> response = page.map(this::toResponse);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/hoje")
    public List<LeituraSensorResponseDTO> listarLeiturasHoje() {
        ZonedDateTime agoraBrasil = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
        LocalDate hojeBrasil = agoraBrasil.toLocalDate();

        LocalDateTime inicio = hojeBrasil.atStartOfDay();
        LocalDateTime fim = hojeBrasil.atTime(23, 59, 59);

        return leituraRepo.findByDataHoraBetween(inicio, fim).stream()
                .map(this::toResponse)
                .toList();
    }

    // --- Mapeamento manual para ResponseDTO
    private LeituraSensorResponseDTO toResponse(LeituraSensor leitura) {
        LeituraSensorResponseDTO dto = new LeituraSensorResponseDTO();
        dto.setId(leitura.getId());
        dto.setIdSensor(leitura.getSensor().getId());
        dto.setValor(leitura.getValor());
        dto.setDataHora(leitura.getDataHora());
        dto.setTipoSensor(leitura.getSensor().getTipoSensor().getTipo());
        return dto;
    }
}


