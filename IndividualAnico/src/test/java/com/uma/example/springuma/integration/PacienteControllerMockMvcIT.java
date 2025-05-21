package com.uma.example.springuma.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.uma.example.springuma.model.Medico;
import com.uma.example.springuma.model.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.LinkedHashMap;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PacienteControllerMockMvcIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /paciente/{id} - Crear un paciente con médico y recuperarlo correctamente")
    void getPacienteByIdWithMedico_shouldReturnCreatedPaciente() throws Exception {
        // Crear un médico
        Medico medico = new Medico();
        medico.setNombre("Dr. Smith");
        medico.setDni("medico-123");
        medico.setEspecialidad("Cardiología");

        mockMvc.perform(post("/medico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isCreated());

        // Recuperar ID asignado automáticamente usando GET por DNI
        MvcResult result = mockMvc.perform(get("/medico/dni/{dni}", "medico-123"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        Integer id = JsonPath.parse(json).read("$.id");

        // Modificar el médico con el ID real
        medico.setId(id);



        // Crear un paciente asociado al médico
        Paciente paciente = new Paciente();
        paciente.setNombre("Juan");
        paciente.setEdad(35);
        paciente.setDni("123-ABC");
        paciente.setCita("2025-06-01");
        paciente.setMedico(medico);             // vínculo con el médico

        mockMvc.perform(post("/paciente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paciente)))
                .andExpect(status().isCreated());
        MvcResult resPac = mockMvc.perform(get("/paciente/medico/{id}", id))
                    .andExpect(status().isOk())
                    .andReturn();

        long pacienteId = JsonPath.parse(resPac.getResponse().getContentAsString())
                                .read("$[0].id", Long.class);

        /* ---------- 5. GET /paciente/{id}  ---------- */
        mockMvc.perform(get("/paciente/{id}", pacienteId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.dni").value("123-ABC"))
                .andExpect(jsonPath("$.medico.id").value((int) id))
                .andExpect(jsonPath("$.medico.nombre").value("Dr. Smith"));

        /* ---------- 6. GET /paciente/medico/{id}  ---------- */
        mockMvc.perform(get("/paciente/medico/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value((int) pacienteId))
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[0].dni").value("123-ABC"));
        }

        @Test
        @DisplayName("PUT /paciente - Crear un paciente, actualizarlo y recuperarlo actualizado")
        void updatePaciente_shouldReturnUpdatedFields() throws Exception {

            /* 1️  CREAR MÉDICO */
            Medico medico = new Medico();
            medico.setNombre("Dr. House");
            medico.setDni("med-999");
            medico.setEspecialidad("Nefrología");

            mockMvc.perform(post("/medico")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(medico)))
                    .andExpect(status().isCreated());

            long medicoId = JsonPath.parse(
                    mockMvc.perform(get("/medico/dni/{dni}", "med-999"))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                    .read("$.id", Long.class);

            medico.setId(medicoId);

            /* 2️ CREAR PACIENTE VINCULADO */
            Paciente paciente = new Paciente();
            paciente.setNombre("Ana");
            paciente.setEdad(28);
            paciente.setDni("pac-001");
            paciente.setCita("2025-07-01");
            paciente.setMedico(medico);

            mockMvc.perform(post("/paciente")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(paciente)))
                    .andExpect(status().isCreated());

            long pacienteId = JsonPath.parse(
                    mockMvc.perform(get("/paciente/medico/{id}", medicoId))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                    .read("$[0].id", Long.class);

            /* 3️  ACTUALIZAR PACIENTE */
            paciente.setId(pacienteId);
            paciente.setNombre("Ana Actualizada");
            paciente.setEdad(29);

            mockMvc.perform(put("/paciente")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(paciente)))
                    .andExpect(status().isNoContent());

            /* 4️  VERIFICAR ACTUALIZACIÓN */
            mockMvc.perform(get("/paciente/{id}", pacienteId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("Ana Actualizada"))
                    .andExpect(jsonPath("$.edad").value(29))
                    .andExpect(jsonPath("$.dni").value("pac-001"))
                    .andExpect(jsonPath("$.medico.id").value((int) medicoId));
        }

        @Test
        @DisplayName("DELETE /paciente/{id} - Crear un paciente, eliminarlo y comprobar que ya no existe")
        void deletePaciente_shouldReturnErrorOnSubsequentGet() throws Exception {

            /* 1️  CREAR MÉDICO */
            Medico medico = new Medico();
            medico.setNombre("Dr. Watson");
            medico.setDni("med-888");
            medico.setEspecialidad("Neurología");

            mockMvc.perform(post("/medico")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(medico)))
                    .andExpect(status().isCreated());

            long medicoId = JsonPath.parse(
                    mockMvc.perform(get("/medico/dni/{dni}", "med-888"))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                    .read("$.id", Long.class);

            medico.setId(medicoId);

            /* 2️ CREAR PACIENTE */
            Paciente paciente = new Paciente();
            paciente.setNombre("Carlos");
            paciente.setEdad(50);
            paciente.setDni("pac-002");
            paciente.setCita("2025-09-01");
            paciente.setMedico(medico);

            mockMvc.perform(post("/paciente")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(paciente)))
                    .andExpect(status().isCreated());

            long pacienteId = JsonPath.parse(
                    mockMvc.perform(get("/paciente/medico/{id}", medicoId))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                    .read("$[0].id", Long.class);

            /* 3️  ELIMINAR PACIENTE */
            mockMvc.perform(delete("/paciente/{id}", pacienteId))
                    .andExpect(status().isOk());

            /* 4️  INTENTAR RECUPERAR → ERROR 5xx */
            mockMvc.perform(get("/paciente/{id}", pacienteId))
                    .andExpect(status().is5xxServerError());
        }

        //Ahora juntamos las funcionalidades y creamos un camino feliz que incluye todas las acciones anteriores
        
        @Test
        @DisplayName("FULL flow pacientes: crear varios → listar → modificar uno → borrar otro → verificar")
        void pacienteCaminoFeliz_shouldCoverFullLifecycle() throws Exception {

        /* ───────────── 1. CREAR MÉDICO ───────────── */
        Medico medico = new Medico();
        medico.setNombre("Dr. Happy");
        medico.setDni("med-happy");
        medico.setEspecialidad("Traumatología");

        mockMvc.perform(post("/medico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isCreated());

        long medicoId = JsonPath.parse(
                mockMvc.perform(get("/medico/dni/{dni}", "med-happy"))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                .read("$.id", Long.class);

        medico.setId(medicoId);

        /* ───────────── 2. CREAR PACIENTE 1 ───────────── */
        Paciente p1 = new Paciente();
        p1.setNombre("Juan");
        p1.setEdad(30);
        p1.setDni("pac-001");
        p1.setCita("2025-01-10");
        p1.setMedico(medico);

        mockMvc.perform(post("/paciente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p1)))
                .andExpect(status().isCreated());

        /* ───────────── 3. CREAR PACIENTE 2 ───────────── */
        Paciente p2 = new Paciente();
        p2.setNombre("Ana");
        p2.setEdad(25);
        p2.setDni("pac-002");
        p2.setCita("2025-02-15");
        p2.setMedico(medico);

        mockMvc.perform(post("/paciente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p2)))
                .andExpect(status().isCreated());

        /* ───────────── 4. LISTAR → deben ser 2 ───────────── */
        String listaJson = mockMvc.perform(get("/paciente/medico/{id}", medicoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Convertimos el JSON en lista de objetos Paciente
        List<Paciente> lista = objectMapper.readValue(
                listaJson,
                new com.fasterxml.jackson.core.type.TypeReference<List<Paciente>>() {});

        // Obtenemos los IDs reales
        long p1Id = lista.stream().filter(p -> "pac-001".equals(p.getDni()))
                        .findFirst().orElseThrow().getId();
        long p2Id = lista.stream().filter(p -> "pac-002".equals(p.getDni()))
                        .findFirst().orElseThrow().getId();

        /* ───────────── 5. MODIFICAR PACIENTE 1 ───────────── */
        p1.setId(p1Id);
        p1.setNombre("Juan Modificado");
        p1.setEdad(31);

        mockMvc.perform(put("/paciente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p1)))
                .andExpect(status().isNoContent());

        // Verificar modificación
        mockMvc.perform(get("/paciente/{id}", p1Id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Modificado"))
                .andExpect(jsonPath("$.edad").value(31));

        /* ───────────── 6. BORRAR PACIENTE 2 ───────────── */
        mockMvc.perform(delete("/paciente/{id}", p2Id))
                .andExpect(status().isOk());

        // Intentar recuperarlo → 5xx
        mockMvc.perform(get("/paciente/{id}", p2Id))
                .andExpect(status().is5xxServerError());

        /* ───────────── 7. LISTAR DE NUEVO → solo 1 ───────────── */
        mockMvc.perform(get("/paciente/medico/{id}", medicoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].dni").value("pac-001"));
        }


}
