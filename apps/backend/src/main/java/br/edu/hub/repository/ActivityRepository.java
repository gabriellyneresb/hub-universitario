// apps/backend/src/main/java/br/edu/hub/repository/ActivityRepository.java

package br.edu.hub.repository;

import br.edu.hub.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findAllByOrderByDateDesc();

    /**
     * Busca atividades cujo título ou descrição contenham o termo informado,
     * sem diferenciar letras maiúsculas de minúsculas, ordenadas pela data mais
     * recente primeiro.
     *
     * Implementa a jornada "Buscar atividades" descrita no {@code PROJECT.md}:
     * a busca é feita no backend e considera tanto o título quanto a descrição.
     *
     * @param titleTerm       termo a ser procurado no título da atividade
     * @param descriptionTerm termo a ser procurado na descrição da atividade
     *                        (normalmente o mesmo valor de {@code titleTerm})
     * @return atividades cujo título ou descrição contenham o termo buscado
     */
    List<Activity> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrderByDateDesc(
            String titleTerm, String descriptionTerm);
}