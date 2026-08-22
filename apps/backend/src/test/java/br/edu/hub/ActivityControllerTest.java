// apps/backend/src/test/java/br/edu/hub/ActivityControllerTest.java

package br.edu.hub;

import br.edu.hub.entity.Activity;
import br.edu.hub.entity.ActivityCategory;
import br.edu.hub.entity.ActivityStatus;
import br.edu.hub.repository.ActivityRepository;
import br.edu.hub.repository.RegistrationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ActivityControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired ActivityRepository activityRepository;
    @Autowired RegistrationRepository registrationRepository;

    private Activity openActivity;
    private Activity fullActivity;

    @BeforeEach
    void setUp() {
        registrationRepository.deleteAll();
        activityRepository.deleteAll();
        openActivity = activityRepository.save(new Activity(
                "Workshop de APIs", "Uma atividade aberta para os testes.", ActivityCategory.WORKSHOP,
                ActivityStatus.OPEN, 2, 0, "Equipe Hub", "Lab 1", LocalDateTime.now().plusDays(2)));
        fullActivity = activityRepository.save(new Activity(
                "Curso lotado", "Uma atividade lotada para os testes.", ActivityCategory.COURSE,
                ActivityStatus.FULL, 1, 1, "Equipe Hub", "Lab 2", LocalDateTime.now().plusDays(3)));
    }

    @Test
    void shouldListActivities() throws Exception {
        mockMvc.perform(get("/api/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldFindExistingActivity() throws Exception {
        mockMvc.perform(get("/api/activities/{id}", openActivity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Workshop de APIs"))
                .andExpect(jsonPath("$.remainingSpots").value(2));
    }

    @Test
    void shouldReturn404ForUnknownActivity() throws Exception {
        mockMvc.perform(get("/api/activities/{id}", 999999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Activity not found"));
    }

    @Test
    void shouldPreventRegistrationWhenActivityIsFull() throws Exception {
        mockMvc.perform(post("/api/activities/{id}/registrations", fullActivity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"studentName":"Maria Souza","studentEmail":"maria@email.com"}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Activity is full"));
    }

    @Test
    void shouldCreateRegistrationForOpenActivity() throws Exception {
        mockMvc.perform(post("/api/activities/{id}/registrations", openActivity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"studentName":"Maria Souza","studentEmail":"maria@email.com"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentEmail").value("maria@email.com"));
    }

    /**
     * {@code GET /api/activities?search=...} deve encontrar atividades pelo
     * título, ignorando diferenças entre maiúsculas e minúsculas.
     */
    @Test
    void shouldFilterActivitiesByTitleSearchTermIgnoringCase() throws Exception {
        mockMvc.perform(get("/api/activities").param("search", "workshop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Workshop de APIs"));
    }

    /**
     * {@code GET /api/activities?search=...} deve encontrar atividades pelo
     * conteúdo da descrição, não apenas pelo título.
     */
    @Test
    void shouldFilterActivitiesByDescriptionSearchTerm() throws Exception {
        mockMvc.perform(get("/api/activities").param("search", "lotada"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Curso lotado"));
    }

    /**
     * Um termo de busca que não corresponde a nenhuma atividade deve resultar
     * em lista vazia com {@code 200 OK}, nunca em erro.
     */
    @Test
    void shouldReturnEmptyListWhenSearchTermMatchesNothing() throws Exception {
        mockMvc.perform(get("/api/activities").param("search", "termo-inexistente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /**
     * Um termo de busca em branco deve ser tratado como "sem busca",
     * retornando o catálogo completo.
     */
    @Test
    void shouldReturnAllActivitiesWhenSearchTermIsBlank() throws Exception {
        mockMvc.perform(get("/api/activities").param("search", "  "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}