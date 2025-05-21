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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.hasSize;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.reactive.server.WebTestClient.*;

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
        @DisplayName("GET /informe/imagen/{id} - Obtener lista de informes correctamente")
        void getInformes_shouldRespondWithListOfInformes() {
        // Crear dos informes asociados a la imagen con ID 1
        // Primer informe (ya creado en init)
        client.post()
                .uri("/informe")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(informe)
                .exchange()
                .expectStatus().isCreated();
        
        // Segundo informe con contenido distinto
        Informe informe2 = new Informe("Predicción modificada", "Contenido modificado", imagen);
        informe2.setId(2L);
        client.post()
                .uri("/informe")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(informe2)
                .exchange()
                .expectStatus().isCreated();
        
        // Consultar la lista de informes asociados a la imagen (se asume que la imagen tiene ID 1)
        List<Informe> informes = client.get()
        .uri("/informe/imagen/1")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(Informe.class)
        .returnResult()
        .getResponseBody();

        assertThat(informes).hasSize(2);
        assertThat(informes.get(0).getContenido()).isEqualTo(informe.getContenido());
        assertThat(informes.get(1).getContenido()).isEqualTo(informe2.getContenido());
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
    
        // Verificar que el informe se haya creado correctamente, se puede hacer tanto usando los objetos como el json
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
        //Ahora juntamos las funcionalidades y creamos un camino feliz que incluye todas las acciones anteriores
        @Test
        @DisplayName("FULL flow informes: crear → leer → listar → borrar y comprobar ausencia")
        void informeCaminoFeliz_shouldCoverFullLifecycle() {

        /* ---------- 1. CREAR PRIMER INFORME (id = 1) ---------- */
        Informe informe1 = new Informe("Predicción A", "Contenido A", imagen);
        client.post()
                .uri("/informe")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(informe1)
                .exchange()
                .expectStatus().isCreated();

        /* 1a. Recuperar y verificar */
        client.get()
                .uri("/informe/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.contenido").isEqualTo("Contenido A");

        /* ---------- 2. CREAR SEGUNDO INFORME (id = 2) ---------- */
        Informe informe2 = new Informe("Predicción B", "Contenido B", imagen);
        client.post()
                .uri("/informe")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(informe2)
                .exchange()
                .expectStatus().isCreated();

        /* ---------- 3. Lista debe contener 2 informes ---------- */
        List<Informe> listaInicial = client.get()
                .uri("/informe/imagen/1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Informe.class)
                .returnResult()
                .getResponseBody();

        assertThat(listaInicial).hasSize(2);

        /* ---------- 4. BORRAR INFORME 1 ---------- */
        client.delete()
                .uri("/informe/1")
                .exchange()
                .expectStatus().isNoContent();

        /* 4a. Recuperarlo ⇒ cuerpo vacío */
        client.get()
                .uri("/informe/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();

        /* 4b. Lista ahora debe contener solo 1 informe */
        List<Informe> listaFinal = client.get()
                .uri("/informe/imagen/1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Informe.class)
                .returnResult()
                .getResponseBody();

        assertThat(listaFinal).hasSize(1);
        assertThat(listaFinal.get(0).getContenido()).isEqualTo("Contenido B");
        }


        }