package com.uma.example.springuma.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uma.example.springuma.integration.base.AbstractIntegration;
import com.uma.example.springuma.model.Informe;
import com.uma.example.springuma.model.Imagen;
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
class InformeControllerWebTestClientIT extends AbstractIntegration {

    @LocalServerPort
    private Integer port;

    private WebTestClient client;

    private Paciente paciente;
    private Imagen imagen;
    private Informe informe;

    @PostConstruct
    public void init() {
        client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .responseTimeout(Duration.ofMillis(30000))
                .build();

        // Crear un paciente
        paciente = new Paciente();
        paciente.setId(1L); // Asignar un ID fijo
        paciente.setNombre("Paciente Test");
        paciente.setEdad(30);
        paciente.setDni("dni-" + System.nanoTime());

        client.post()
                .uri("/paciente")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(paciente)
                .exchange()
                .expectStatus().isCreated();

        // Subir una imagen asociada al paciente
        File img = new File("./src/test/resources/healthy.png");
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("image", new FileSystemResource(img));
        builder.part("paciente", paciente, MediaType.APPLICATION_JSON);

        FluxExchangeResult<String> uploadResponse = client.post()
                .uri("/imagen")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class);

        String uploadResult = uploadResponse.getResponseBody().blockFirst();
        assertEquals("{\"response\" : \"file uploaded successfully : healthy.png\"}", uploadResult);

        // Crear un informe asociado a la imagen
        imagen = new Imagen();
        imagen.setId(1L); // ID de la imagen subida
        imagen.setNombre("healthy.png");
        informe = new Informe("Predicción inicial", "Contenido inicial", imagen);
        informe.setId(1L);
    }

    @Test
    @DisplayName("POST /informe - Crear un informe correctamente")
    void createInforme_shouldRespondSuccess() {
        // Crear un informe
        client.post()
                .uri("/informe")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(informe)
                .exchange()
                .expectStatus().isCreated();
    
        // Obtener el informe creado
        FluxExchangeResult<Informe> result = client.get()
                .uri("/informe/1") // ID del informe creado
                .exchange()
                .expectStatus().isOk()
                .returnResult(Informe.class);
    
        Informe informeObtained = result.getResponseBody().blockFirst();
    
        // Validar que el campo "prediccion" contiene un valor generado
        assert informeObtained != null;
        assertEquals(informe.getContenido(), informeObtained.getContenido());
        assert informeObtained.getPrediccion().contains("status");
        assert informeObtained.getPrediccion().contains("score");
    }
    @Test
    @DisplayName("POST /informe - Crear y eliminar un informe correctamente")
    void createAndDeleteInforme_shouldRespondSuccess() {
        // Crear un informe
        client.post()
                .uri("/informe")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(informe)
                .exchange()
                .expectStatus().isCreated();
    
        // Verificar que el informe se haya creado correctamente
        client.get()
                .uri("/informe/1") // ID del informe creado
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.prediccion").isNotEmpty()
                .jsonPath("$.contenido").isEqualTo(informe.getContenido());
    
        // Eliminar el informe
        client.delete()
                .uri("/informe/1") // ID del informe creado
                .exchange()
                .expectStatus().isNoContent();
    
        // Verificar que el informe ya no esté disponible
        client.get()
                .uri("/informe/1")
                .exchange()
                .expectStatus().isOk() // Verificar que la respuesta sea exitosa
                .expectBody().isEmpty(); // Verificar que el cuerpo esté vacío
    }


}