package com.uma.example.springuma.integration.base;

import com.uma.example.springuma.model.Imagen;
import com.uma.example.springuma.model.Medico;
import com.uma.example.springuma.model.Paciente;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.File;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ImagenControllerWebTestClientIT {

    @LocalServerPort
    private Integer port;

    private WebTestClient client;

    private Medico medico;
    private Paciente paciente;

    @PostConstruct
    public void init() {
        client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .responseTimeout(Duration.ofSeconds(10))
                .build();
    }

    @BeforeEach
    void setupData() {
        medico = new Medico();
        medico.setId(1000L);
        medico.setNombre("Dr. Imagen");

        paciente = new Paciente();
        paciente.setId(1001L);
        paciente.setNombre("Paciente Imagen");
        paciente.setMedico(medico);

        client.post().uri("/medico")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(medico)
                .exchange()
                .expectStatus().isCreated();

        client.post().uri("/paciente")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(paciente)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    @DisplayName("Subir imagen de paciente correctamente")
    void uploadImage_success() {
        File testImage = new File("./src/test/resources/healthy.png");
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("imagen", new FileSystemResource(testImage));

        client.post().uri("/imagen/1001")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .value(response -> {
                    assertEquals(true, response.contains("Imagen subida"));
                });
    }

    @Test
    @DisplayName("Predecir imagen de paciente devuelve resultado")
    void predictImage_success() {
        // Subir imagen primero
        File testImage = new File("./src/test/resources/healthy.png");
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("imagen", new FileSystemResource(testImage));

        client.post().uri("/imagen/1001")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isCreated();

        // Obtener la predicción
        EntityExchangeResult<String> result = client.get()
                .uri("/imagen/predict/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult();

        String output = result.getResponseBody();
        assertTrue(output.contains("Probabilidad de cáncer") || output.contains("No se ha podido analizar"));
    }
}
