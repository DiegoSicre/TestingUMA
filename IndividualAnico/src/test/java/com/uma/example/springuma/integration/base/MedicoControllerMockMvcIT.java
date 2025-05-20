package com.uma.example.springuma.integration.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uma.example.springuma.model.Medico;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class MedicoControllerMockMvcIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Crear un médico correctamente")
    void createMedico_successfully() throws Exception {
        // Arrange
        Medico medico = new Medico();
        medico.setId(1L);
        medico.setNombre("Dr. House");

        // Act & Assert
        mockMvc.perform(post("/medico")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Dr. House")));
    }

    @Test
    @DisplayName("Obtener todos los médicos debe devolver una lista con al menos un médico")
    void getAllMedicos_successfully() throws Exception {
        // Arrange
        Medico medico = new Medico();
        medico.setId(2L);
        medico.setNombre("Dr. Strange");
        // Act 
        mockMvc.perform(post("/medico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isCreated());

        // Assert
        mockMvc.perform(get("/medicos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", not(empty())));

        mockMvc.perform(get("/medicos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre", containsString("Dr. Strange")));
    }

    @Test
    @DisplayName("Actualizar un médico existente correctamente")
    void updateMedico_successfully() throws Exception {
        // Arrange
        Medico medico = new Medico();
        medico.setId(3L);
        medico.setNombre("Dr. Old");

        // Act
        mockMvc.perform(post("/medico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isCreated());

        medico.setNombre("Dr. Updated");

        // Assert
        mockMvc.perform(put("/medico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Updated")));

        mockMvc.perform(get("/medicos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[?(@.id==3)].nombre").value("Dr. Updated"));
    }

    @Test
    @DisplayName("Eliminar un médico correctamente")
    void deleteMedico_successfully() throws Exception {
        // Arrange
        Medico medico = new Medico();
        medico.setId(4L);
        medico.setNombre("Dr. Who");

        mockMvc.perform(post("/medico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isCreated());

        // Act & Assert
        mockMvc.perform(delete("/medico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Eliminado")));

        mockMvc.perform(get("/medicos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[?(@.id==4)]", empty()));
    }

    @Test
    @DisplayName("Eliminar un médico que no existe debe devolver error")
    void deleteNonexistentMedico_returnsError() throws Exception {
        // Arrange
        Medico medico = new Medico();
        medico.setId(999L);
        medico.setNombre("Ficticio");

        // Act & Assert
        mockMvc.perform(delete("/medico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().is5xxServerError());

        
    }

    @Test
    @DisplayName("Editar un médico y comprobar que se ha modificado")
    void editMedico_andVerifyUpdate() throws Exception {
        // Arrange
        Medico medico = new Medico();
        medico.setId(5L);
        medico.setNombre("Dr. Original");

        mockMvc.perform(post("/medico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isCreated());

        // Act
        medico.setNombre("Dr. Editado");
        mockMvc.perform(put("/medico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isOk());

        // Assert
        mockMvc.perform(get("/medicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id==5)].nombre").value("Dr. Editado"));
    }

        @Test
        @DisplayName("Camino feliz: Crear, Modificar, Obtener y Eliminar un médico")
        void happyPath_create_modify_get_delete_medico() throws Exception {
        // Crear un médico
        Medico medico = new Medico();
        medico.setId(6L);
        medico.setNombre("Dr. Camino Feliz");
        mockMvc.perform(post("/medico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Dr. Camino Feliz")));
        // Modificar el médico
        medico.setNombre("Dr. Modificado");
        mockMvc.perform(put("/medico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Modificado")));
        // Obtener el médico modificado
        mockMvc.perform(get("/medicos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[?(@.id==6)].nombre").value("Dr. Modificado"));
        // Eliminar el médico
        mockMvc.perform(delete("/medico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Eliminado")));
        // Comprobar que el médico ha sido eliminado
        mockMvc.perform(get("/medicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id==6)]", empty()));
        }
}

