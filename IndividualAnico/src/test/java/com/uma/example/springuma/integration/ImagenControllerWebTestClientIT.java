package com.uma.example.springuma.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uma.example.springuma.integration.base.AbstractIntegration;
import com.uma.example.springuma.model.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ImagenControllerWebTestClientIT extends AbstractIntegration {

    @LocalServerPort
    private Integer port;

    private WebTestClient client;

    private Paciente paciente;

    @PostConstruct
    public void init() {
        client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .responseTimeout(Duration.ofMillis(30000))
                .build();

        // Asignar directamente un ID al paciente
        paciente = new Paciente();
        paciente.setId(1L); // Asignar un ID fijo
        paciente.setNombre("Paciente Test");
        paciente.setEdad(30);
        paciente.setDni("dni-" + System.nanoTime());
    }

    @Test
    @DisplayName("POST /imagen - Subida de imagen multipart correctamente")
    void uploadImagePost_shouldRespondSuccess() throws Exception {
        // Crear un paciente
        client.post()
                .uri("/paciente")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(paciente)
                .exchange()
                .expectStatus().isCreated();
    
        // Preparar el archivo de imagen
        File img = new File("./src/test/resources/healthy.png");
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("image", new FileSystemResource(img));
        builder.part("paciente", paciente, MediaType.APPLICATION_JSON); // Pasar el objeto directamente
    
        // Subir la imagen
        FluxExchangeResult<String> response = client.post()
                .uri("/imagen")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class);
    
        String result = response.getResponseBody().blockFirst();
        assertEquals("{\"response\" : \"file uploaded successfully : healthy.png\"}", result);
    }

    @Test
    @DisplayName("GET /imagen/predict/{id} - Realizar predicción de una imagen correctamente")
    void predictImageGet_shouldRespondValidPredict() throws Exception {
        // Configurar datos de prueba en la base de datos
        Long imagenId = 1L; // ID de la imagen preconfigurada en la base de datos
    
        // Obtener la predicción de la imagen
        client.get()
                .uri("/imagen/predict/" + imagenId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk() // Verificar que el estado HTTP sea 200 OK
                .expectHeader().contentType(MediaType.APPLICATION_JSON) // Verificar que el Content-Type sea JSON
                .expectBody()
                .jsonPath("$.status").value(status -> {
                    // Validar que el campo "status" contiene "Cancer" o "Not cancer"
                    assertEquals(true, status.equals("Cancer") || status.equals("Not cancer"));
                })
                .jsonPath("$.score").value(score -> {
                    // Validar que el campo "score" es un número válido entre 0.0 y 1.0
                    assert score instanceof Double;
                    assert (Double) score >= 0.0 && (Double) score <= 1.0;
                });
    }
}