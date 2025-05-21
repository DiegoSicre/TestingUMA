package com.uma.example.springuma.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.uma.example.springuma.model.Medico;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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


        /*
        TEST CAMINO FELIZ CON TODAS LAS FUNCIONALIDADES
        */
        @Test
        @DisplayName("FULL flow médico: crear → leer → modificar → leer por ID y DNI → borrar → comprobar ausencia")
        void medicoCaminoFeliz_shouldCoverFullLifecycle() throws Exception {

                /* ---------- 1. CREAR MÉDICO ---------- */
                Medico medico = new Medico();
                medico.setNombre("Dr. Camino Feliz");
                medico.setDni("dni-999");
                medico.setEspecialidad("Dermatología");

                mockMvc.perform(post("/medico")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medico)))
                        .andExpect(status().isCreated());

                /* ---------- 2. OBTENER ID REAL VÍA GET /medico/dni/{dni} ---------- */
                MvcResult resMed = mockMvc.perform(get("/medico/dni/{dni}", "dni-999"))
                        .andExpect(status().isOk())
                        .andReturn();

                long id = JsonPath.parse(resMed.getResponse().getContentAsString())
                                .read("$.id", Long.class);

                /* ---------- 3. VERIFICAR CON GET /medico/{id} ---------- */
                mockMvc.perform(get("/medico/{id}", id))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.nombre").value("Dr. Camino Feliz"))
                        .andExpect(jsonPath("$.dni").value("dni-999"));

                /* ---------- 4. MODIFICAR EL MÉDICO ---------- */
                medico.setId(id);
                medico.setNombre("Dr. C. Feliz Modificado");

                mockMvc.perform(put("/medico")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medico)))
                        .andExpect(status().isNoContent());          // 204

                /* ---------- 5. VERIFICAR CAMBIOS POR ID Y POR DNI ---------- */
                mockMvc.perform(get("/medico/{id}", id))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.nombre").value("Dr. C. Feliz Modificado"));

                mockMvc.perform(get("/medico/dni/{dni}", "dni-999"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.nombre").value("Dr. C. Feliz Modificado"));

                /* ---------- 6. BORRAR MÉDICO ---------- */
                mockMvc.perform(delete("/medico/{id}", id))
                        .andExpect(status().isOk());

                /* ---------- 7. COMPROBAR QUE YA NO EXISTE ---------- */
                mockMvc.perform(get("/medico/{id}", id))
                        .andExpect(status().is5xxServerError());   // el controlador devuelve 500

                mockMvc.perform(get("/medico/dni/{dni}", "dni-999"))
                        .andExpect(status().isNotFound());        // devuelve 404 cuando no existe
        }

        /*
        TEST CAMINOS FELICES MÁS AISLADOS
        */

        @Test
        @DisplayName("GET /medico/dni/{dni} - Crear, actualizar y recuperar médico correctamente por DNI")
        void getMedicoByDni_shouldReturnUpdatedMedico() throws Exception {
                // Arrange
                Medico medico = new Medico();
                medico.setDni("abc-123");
                medico.setNombre("Dr. Original");
                medico.setEspecialidad("Traumatología");

                // Crear médico
                mockMvc.perform(post("/medico")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medico)))
                        .andExpect(status().isCreated());

                // Recuperar ID asignado automáticamente usando GET por DNI
                MvcResult result = mockMvc.perform(get("/medico/dni/{dni}", "abc-123"))
                        .andExpect(status().isOk())
                        .andReturn();

                String json = result.getResponse().getContentAsString();
                Integer id = JsonPath.parse(json).read("$.id");

                // Modificar el médico con el ID real
                medico.setId(id);
                medico.setNombre("Dr. Actualizado");

                // Actualizar médico
                mockMvc.perform(put("/medico")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medico)))
                        .andExpect(status().isNoContent());

                // Act & Assert: recuperar por DNI
                mockMvc.perform(get("/medico/dni/{dni}", "abc-123"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.nombre").value("Dr. Actualizado"))
                        .andExpect(jsonPath("$.dni").value("abc-123"));
        }


        @Test
        @DisplayName("GET /medico/{id} - Crear, eliminar y comprobar que el médico ya no existe")
        void getMedicoById_shouldFailAfterDeletion() throws Exception {
                // Arrange
                Medico medico = new Medico();

                medico.setNombre("Dr. Temporal");
                medico.setDni("temp-456");

        

                // Crear médico
                mockMvc.perform(post("/medico")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medico)))
                        .andExpect(status().isCreated());
                //Recuperarlo para ver el id asignado por la BBDD        
                MvcResult result = mockMvc.perform(get("/medico/dni/{dni}", "temp-456"))
                        .andExpect(status().isOk())
                        .andReturn();

                String json = result.getResponse().getContentAsString();
                Integer id = JsonPath.parse(json).read("$.id");

                // Modificar el médico con el ID real
                medico.setId(id);                

                // Eliminar médico
                mockMvc.perform(delete("/medico/{id}", id))
                        .andExpect(status().isOk());

                // Act & Assert: intentar recuperar por ID
                mockMvc.perform(get("/medico/{id}", id))
                        .andExpect(status().is5xxServerError()); // el controlador devuelve 500 si no lo encuentra
        }



}

