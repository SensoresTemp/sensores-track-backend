package sensores.track.backend.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sensores.track.backend.model.ConfiguracaoAlerta;
import sensores.track.backend.model.Conta;
import sensores.track.backend.model.LeituraSensor;
import sensores.track.backend.model.dto.LeituraSensorDTO;
import sensores.track.backend.model.Sensor;
import sensores.track.backend.model.dto.LeituraSensorResponseDTO;
import sensores.track.backend.repository.ConfiguracaoAlertaRepository;
import sensores.track.backend.repository.ContaRepository;
import sensores.track.backend.repository.LeituraSensorRepository;
import sensores.track.backend.repository.SensorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sensores.track.backend.service.EmailService;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;

@RestController
@RequestMapping("/api/leitura/sensor")
@RequiredArgsConstructor
public class LeituraSensorController {

    private final LeituraSensorRepository leituraRepo;
    private final SensorRepository sensorRepo;
    private final ContaRepository contaRepo;
    private final ConfiguracaoAlertaRepository alertaRepo;
    private final EmailService emailService;

    /**
     * Registra uma nova leitura de sensor no sistema.
     * Converte a data/hora atual para o fuso horário de São Paulo.
     */
    @PostMapping
    public ResponseEntity<Void> registrarLeitura(@Valid @RequestBody LeituraSensorDTO dto) {

        Sensor sensor = sensorRepo.findById(dto.getIdSensor())
                .orElseThrow(() -> new EntityNotFoundException("Sensor não encontrado"));

        Conta conta = contaRepo.findById(dto.getIdConta())
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        ZonedDateTime dataHoraBrasil = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
        LeituraSensor leitura = new LeituraSensor(null, sensor, dto.getValor(), dataHoraBrasil.toLocalDateTime(), conta);

        leituraRepo.save(leitura);

        // Verifica se leitura está fora dos limites
        BigDecimal valor = dto.getValor();
        if (sensor.getLimiteMinimoPpm() != null && valor.compareTo(sensor.getLimiteMinimoPpm()) < 0 ||
                sensor.getLimitePpm() != null && valor.compareTo(sensor.getLimitePpm()) > 0) {

            // Busca os emails configurados
            List<ConfiguracaoAlerta> destinatarios = alertaRepo.findAll();

            for (ConfiguracaoAlerta alerta : destinatarios) {
                try {
                    emailService.sendAlertEmail(
                            alerta.getEmail(),
                            sensor.getTipoSensor().getTipo(),
                            valor,
                            sensor.getLimiteMinimoPpm(),
                            sensor.getLimitePpm()
                    );
                } catch (Exception e) {
                    System.err.println("Erro ao enviar e-mail para " + alerta.getEmail() + ": " + e.getMessage());
                }
            }
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Lista todas as leituras registradas no sistema (sem filtro).
     */
    @GetMapping
    public List<LeituraSensorResponseDTO> listarTodas() {
        return leituraRepo.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Lista todas as leituras de um sensor específico, identificado pelo seu ID e pela conta.
     * Se o ID da conta não for informado, assume conta 1 como padrão.
     */
    @GetMapping("/{idSensor}")
    public List<LeituraSensorResponseDTO> listarPorSensor(
            @PathVariable Long idSensor,
            @RequestParam(name = "idConta", required = false) Long idConta
    ) {
        if (idConta == null) {
            idConta = 1L;
        }

        return leituraRepo.findBySensorIdAndConta_Id(idSensor, idConta)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Retorna a última leitura registrada para um sensor específico.
     */
    @GetMapping("/{idSensor}/ultima-leitura")
    public ResponseEntity<LeituraSensorResponseDTO> obterUltimaLeitura(@PathVariable Long idSensor, @RequestParam(name = "idConta", required = false) Long idConta) {

        if (idConta == null) {
            idConta = 1L;
        }

        LeituraSensor ultima = leituraRepo.findTop1BySensorIdAndConta_IdOrderByIdDesc(idSensor, idConta);

        if (ultima == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toResponse(ultima));
    }

    /**
     * Lista as leituras filtrando pelo tipo de sensor (ex: "Temperatura", "Umidade").
     */
    @GetMapping("/por-tipo")
    public List<LeituraSensorResponseDTO> listarPorTipo(@RequestParam String tipo, @RequestParam(name = "idConta", required = false) Long idConta) {

        if (idConta == null) {
            idConta = 1L;
        }

        return leituraRepo.findByTipoSensor(tipo, idConta).stream().map(this::toResponse).toList();
    }

    /**
     * Lista as leituras de um tipo específico de sensor, dentro de um intervalo de datas.
     */
    @GetMapping("/por-tipo/intervalo")
    public List<LeituraSensorResponseDTO> listarPorTipoEData(@RequestParam String tipo,
                                                             @RequestParam LocalDateTime dataInicio,
                                                             @RequestParam LocalDateTime dataFim,
                                                             @RequestParam(name = "idConta", required = false) Long idConta) {

        if (idConta == null) {
            idConta = 1L;
        }

        return leituraRepo.findByTipoSensorAndData(tipo, dataInicio, dataFim, idConta).stream().map(this::toResponse).toList();
    }

    /**
     * Versão paginada da listagem por tipo de sensor e intervalo de datas.
     * Permite obter os resultados em páginas usando o objeto Pageable.
     */
    @GetMapping("/por-tipo/intervalo/paginado")
    public ResponseEntity<Page<LeituraSensorResponseDTO>> listarPorTipoEDataPaginado(
            @RequestParam String tipo,
            @RequestParam LocalDateTime dataInicio,
            @RequestParam LocalDateTime dataFim,
            @RequestParam(name = "idConta", required = false) Long idConta,
            Pageable pageable) {

        if (idConta == null) {
            idConta = 1L;
        }

        Page<LeituraSensor> page = leituraRepo.findByTipoSensorAndDataPaginado(tipo, dataInicio, dataFim, idConta, pageable);
        Page<LeituraSensorResponseDTO> response = page.map(this::toResponse);

        return ResponseEntity.ok(response);
    }

    /**
     * Lista todas as leituras registradas no dia atual (hoje), com base no horário de São Paulo.
     */
    @GetMapping("/hoje")
    public List<LeituraSensorResponseDTO> listarLeiturasHoje(@RequestParam(name = "idConta", required = false) Long idConta) {
        ZonedDateTime agoraBrasil = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
        LocalDate hojeBrasil = agoraBrasil.toLocalDate();

        LocalDateTime inicio = hojeBrasil.atStartOfDay();
        LocalDateTime fim = hojeBrasil.atTime(23, 59, 59);

        if (idConta == null) {
            idConta = 1L;
        }

        return leituraRepo.findByDataHoraBetweenAndConta_Id(inicio, fim, idConta).stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Converte a entidade LeituraSensor para um DTO de resposta, adicionando informações do tipo de sensor.
     */
    private LeituraSensorResponseDTO toResponse(LeituraSensor leitura) {
        LeituraSensorResponseDTO dto = new LeituraSensorResponseDTO();
        dto.setId(leitura.getId());
        dto.setIdSensor(leitura.getSensor().getId());
        dto.setValor(leitura.getValor());
        dto.setDataHora(leitura.getDataHora());
        dto.setTipoSensor(leitura.getSensor().getTipoSensor().getTipo());
        dto.setIdConta(leitura.getConta().getId());
        return dto;
    }
}


