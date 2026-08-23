// apps/backend/src/main/java/br/edu/hub/service/ActivityService.java
package br.edu.hub.service;

import br.edu.hub.dto.ActivityResponse;
import br.edu.hub.dto.ActivityUpdateRequest;
import br.edu.hub.entity.Activity;
import br.edu.hub.exception.ActivityNotFoundException;
import br.edu.hub.repository.ActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Transactional(readOnly = true)
    public List<ActivityResponse> list(String search) {
        return activityRepository.findAllByOrderByDateDesc().stream()
                .map(ActivityResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ActivityResponse findById(Long id) {
        return ActivityResponse.from(requireActivity(id));
    }

    @Transactional
    public ActivityResponse update(Long id, ActivityUpdateRequest request) {
        Activity activity = requireActivity(id);
        if (request.title() != null) activity.setTitle(request.title());
        if (request.description() != null) activity.setDescription(request.description());
        if (request.category() != null) activity.setCategory(request.category());
        if (request.status() != null) activity.setStatus(request.status());
        if (request.capacity() != null) activity.setCapacity(request.capacity());
        if (request.organizer() != null) activity.setOrganizer(request.organizer());
        if (request.location() != null) activity.setLocation(request.location());
        if (request.date() != null) activity.setDate(request.date());
        return ActivityResponse.from(activityRepository.save(activity));
    }

    /**
     * Busca uma atividade pelo identificador, lançando um erro tratável como recurso não
     * encontrado quando ela não existe.
     *
     * <p>Anteriormente este método lançava {@link IllegalArgumentException}, que era
     * interpretada pelo {@code GlobalExceptionHandler} como {@code 500 Internal Server Error}.
     * Isso divergia do contrato do produto, que exige {@code 404 Not Found} para identificadores
     * inexistentes (ver seção "Consultar detalhes" do {@code PROJECT.md}). Agora o método lança
     * {@link ActivityNotFoundException}, tratada explicitamente como {@code 404}.</p>
     *
     * @param id identificador da atividade.
     * @return a entidade {@link Activity} encontrada.
     * @throws ActivityNotFoundException quando não existe atividade com o {@code id} informado.
     */
    public Activity requireActivity(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new ActivityNotFoundException("Activity not found"));
    }
}