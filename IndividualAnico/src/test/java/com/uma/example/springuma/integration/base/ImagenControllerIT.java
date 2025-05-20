package com.uma.example.springuma.integration.base;



import com.uma.example.springuma.integration.base.AbstractIntegration;
import com.uma.example.springuma.model.Paciente;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;

class ImagenControllerIT extends AbstractIntegration {

    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void uploadImage_WithValidPaciente_ReturnsOk() {
        // Arrange
        File imageFile = new File("src/test/resources/healthy.png");
        FileSystemResource imageResource = new FileSystemResource(imageFile);

        Paciente paciente = new Paciente();
        paciente.setId(1L); // Asumimos que el paciente ya existe, o se ignora este campo
        paciente.setNombre("Paciente Test");

        // Convertir paciente a JSON
        String pacienteJson = """
            {
              "id": 1,
              "nombre": "Paciente Test"
            }
        """;

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("image", imageResource);
        parts.add("paciente", pacienteJson);

        // Act & Assert
        webTestClient.post()
                .uri("/imagen")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(parts)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.response").value(val -> {
                    assert val.toString().contains("file uploaded successfully");
                });
    }
}
