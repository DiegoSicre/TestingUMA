package com.uma.example.springuma.integration.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uma.example.springuma.model.Medico;
import com.uma.example.springuma.model.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PacienteControllerMockMvcIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Crear un paciente y asociarlo a un médico")
    void createPaciente_associateToMedico() throws Exception {
        // Arrange
        Medico medico = new Medico();
        medico.setId(10L);
        medico.setNombre("Dr. Asignador");

        mockMvc.perform(post("/medico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isCreated());

        Paciente paciente = new Paciente();
        paciente.setId(100L);
        paciente.setNombre("Paciente Uno");
        paciente.setMedico(medico);

        // Act & Assert
        mockMvc.perform(post("/paciente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paciente)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Paciente Uno")));
    }

    @Test
    @DisplayName("Editar un paciente y verificar el cambio de médico")
    void updatePaciente_andChangeMedico() throws Exception {
        // Arrange
        Medico medico1 = new Medico();
        medico1.setId(20L);
        medico1.setNombre("Dr. Original");

        Medico medico2 = new Medico();
        medico2.setId(21L);
        medico2.setNombre("Dr. Nuevo");

        mockMvc.perform(post("/medico").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(medico1))).andExpect(status().isCreated());
        mockMvc.perform(post("/medico").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(medico2))).andExpect(status().isCreated());

        Paciente paciente = new Paciente();
        paciente.setId(200L);
        paciente.setNombre("Paciente Cambio");
        paciente.setMedico(medico1);

        mockMvc.perform(post("/paciente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paciente)))
                .andExpect(status().isCreated());

        // Act
        paciente.setMedico(medico2);

        mockMvc.perform(put("/paciente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paciente)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Dr. Nuevo")));
    }

    @Test
    @DisplayName("Eliminar un paciente correctamente")
    void deletePaciente_successfully() throws Exception {
        // Arrange
        Medico medico = new Medico();
        medico.setId(30L);
        medico.setNombre("Dr. Baja");

        Paciente paciente = new Paciente();
        paciente.setId(300L);
        paciente.setNombre("Paciente Baja");
        paciente.setMedico(medico);

        mockMvc.perform(post("/medico").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(medico))).andExpect(status().isCreated());
        mockMvc.perform(post("/paciente").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(paciente))).andExpect(status().isCreated());

        // Act & Assert
        mockMvc.perform(delete("/paciente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paciente)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Eliminado")));
    }
}
