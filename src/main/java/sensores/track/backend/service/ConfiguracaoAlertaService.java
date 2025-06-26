package sensores.track.backend.service;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sensores.track.backend.model.ConfiguracaoAlerta;
import sensores.track.backend.repository.ConfiguracaoAlertaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfiguracaoAlertaService {

    private final ConfiguracaoAlertaRepository alertaRepo;

    public ConfiguracaoAlerta salvar(String email) {
        alertaRepo.findByEmail(email).ifPresent(existing -> {
            throw new EntityExistsException("E-mail já cadastrado para alertas");
        });

        return alertaRepo.save(ConfiguracaoAlerta.builder().email(email).build());
    }

    public List<ConfiguracaoAlerta> listarTodos() {
        return alertaRepo.findAll();
    }

    public void excluir(Long id) {
        alertaRepo.deleteById(id);
    }
}
