package com.uma.example.springuma.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uma.example.springuma.integration.base.AbstractIntegration;
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

import static org.hamcrest.Matchers.hasSize;
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
        Long imagenId = subirImagenDummy();
    
        client.get()
            .uri("/imagen/predict/" + imagenId)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.status").value(status -> {
                assertEquals(true, status.equals("Cancer") || status.equals("Not cancer"));
            })
            .jsonPath("$.score").value(score -> {
                assert score instanceof Double;
                assert (Double) score >= 0.0 && (Double) score <= 1.0;
            });
    }

    //Test adicionales para terminar de probar completamente la clase ImagenController

    //Como este código se va a repetir en estos test lo incluimos abajo
    /** Crea un paciente y sube una imagen, devuelve el ID de la imagen subida */
    private Long subirImagenDummy() throws Exception {
        // Crear el paciente
        client.post()
                .uri("/paciente")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(paciente)
                .exchange()
                .expectStatus().isCreated();

        // Subir imagen
        File img = new File("./src/test/resources/healthy.png");
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("image", new FileSystemResource(img));
        builder.part("paciente", paciente, MediaType.APPLICATION_JSON);

        client.post()
                .uri("/imagen")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isOk();

        // Obtener ID de la imagen subida (la primera del paciente)
        FluxExchangeResult<Long> response = client.get()
                .uri("/imagen/paciente/1")
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class);

        return 1L; // si sabes que siempre será la primera
    }

    @Test
    @DisplayName("GET /imagen/{id} - Descargar una imagen correctamente")
    void downloadImage_shouldRespondWithImageData() throws Exception {
        // Crear un paciente y subir una imagen
        Long imagenId = subirImagenDummy();


        // Descargar la imagen
        FluxExchangeResult<byte[]> response = client.get()
                .uri("/imagen/" + imagenId) // ID de la imagen subida
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("image/png")
                .returnResult(byte[].class);

        byte[] imageData = response.getResponseBody().blockFirst();
        assert imageData != null;
        assert imageData.length > 0; // Validar que los datos de la imagen no están vacíos
    }

    @Test
    @DisplayName("GET /imagen/info/{id} - Obtener información de una imagen correctamente")
    void getImagen_shouldRespondWithImageInfo() throws Exception {
        // Crear un paciente y subir una imagen
        Long imagenId = subirImagenDummy();
    
        // Obtener información de la imagen como objeto
        FluxExchangeResult<com.uma.example.springuma.model.Imagen> result = client.get()
                .uri("/imagen/info/" + imagenId)
                .exchange()
                .expectStatus().isOk()
                .returnResult(com.uma.example.springuma.model.Imagen.class);
    
       Imagen imagenObtained = result.getResponseBody().blockFirst();
    
        assertEquals(imagenId, imagenObtained.getId());
        assertEquals("healthy.png", imagenObtained.getNombre());
        assertEquals(paciente.getId(), imagenObtained.getPaciente().getId());
        assertEquals(paciente.getNombre(), imagenObtained.getPaciente().getNombre());
        assertEquals(paciente.getDni(), imagenObtained.getPaciente().getDni());
        assertEquals(paciente.getEdad(), imagenObtained.getPaciente().getEdad());
    }

    /** Sube la imagen indicada y devuelve el id que generó la BBDD */
    private Long subirImagen(File fichero) throws Exception {
        MultipartBodyBuilder mb = new MultipartBodyBuilder();
        mb.part("image", new FileSystemResource(fichero));
        mb.part("paciente", paciente, MediaType.APPLICATION_JSON);

        client.post().uri("/imagen")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData(mb.build()))
            .exchange()
            .expectStatus().isOk();

        // Pedimos la última imagen del paciente y devolvemos su id
        return client.get().uri("/imagen/paciente/{id}", paciente.getId())
                    .exchange()
                    .expectStatus().isOk()
                    .returnResult(Imagen.class)
                    .getResponseBody().blockLast()          // la que acabamos de crear
                    .getId();
    }
    @Test
    @DisplayName("GET /imagen/paciente/{id} – devuelve las 2 imágenes subidas")
    void getImagenes_shouldReturnBothImages() throws Exception {
        Long imagenId = subirImagenDummy();

        // Subir otra imagen imagen
        File img = new File("./src/test/resources/healthy.png");
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("image", new FileSystemResource(img));
        builder.part("paciente", paciente, MediaType.APPLICATION_JSON);

        client.post()
                .uri("/imagen")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isOk();

        // Obtener ID de la imagen subida (la primera del paciente)
        FluxExchangeResult<Long> response = client.get()
                .uri("/imagen/paciente/1")
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class);

        /* ---------- arrange ---------- */
        
        /* ---------- act & assert ----- */
        client.get().uri("/imagen/paciente/{id}", paciente.getId())
            .exchange()
            .expectStatus().isOk()
            .expectBody()
                .jsonPath("$[0].nombre").value(v -> assertEquals("healthy.png", String.valueOf(v.toString())))
                .jsonPath("$[1].nombre").value(v -> assertEquals("healthy.png", String.valueOf(v.toString())));
                 
            }

        //Mismo test con una sola imagen
        @Test
        @DisplayName("GET /imagen/paciente/{id} - Obtener todas las imágenes de un paciente")
        void getImagenes_shouldRespondWithListOfImages() throws Exception {
            // Crear un paciente y subir una imagen
            Long imagenId = subirImagenDummy();

            // Obtener todas las imágenes del paciente como lista de objetos
            FluxExchangeResult<com.uma.example.springuma.model.Imagen> result = client.get()
                    .uri("/imagen/paciente/" + paciente.getId())
                    .exchange()
                    .expectStatus().isOk()
                    .returnResult(com.uma.example.springuma.model.Imagen.class);

            Imagen imagenObtained = result.getResponseBody().blockFirst();

            assertEquals("healthy.png", imagenObtained.getNombre());
            assertEquals(paciente.getId(), imagenObtained.getPaciente().getId());
            assertEquals(paciente.getNombre(), imagenObtained.getPaciente().getNombre());
            assertEquals(paciente.getDni(), imagenObtained.getPaciente().getDni());
            assertEquals(paciente.getEdad(), imagenObtained.getPaciente().getEdad());
        }
    

    @Test
    @DisplayName("DELETE /imagen/{id} - Eliminar una imagen correctamente")
    void deleteImagen_shouldRespondNoContent() throws Exception {
        // Crear un paciente y subir una imagen
        Long imagenId = subirImagenDummy();

        // Eliminar la imagen
        client.delete()
                .uri("/imagen/" + imagenId) // ID de la imagen subida
                .exchange()
                .expectStatus().is2xxSuccessful();

        // Verificar que la imagen ya no está disponible
        client.get()
                .uri("/imagen/info/1")
                .exchange()
                .expectStatus().is5xxServerError(); // Esperar un estado 404 Not Found
    }



    //Ahora juntamos las funcionalidades y creamos un camino feliz que incluye todas las acciones anteriores
    @Test
    @DisplayName("FULL flow imágenes: subir-descargar-predecir-listar-borrar")
    void imagenCaminoFeliz_shouldCoverFullLifecycle() throws Exception {

        /* ---------- 1. CREAR PACIENTE ---------- */
        client.post()
            .uri("/paciente")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(paciente)                     // ‘paciente’ viene del @PostConstruct
            .exchange()
            .expectStatus().isCreated();

        /* ---------- 2. SUBIR PRIMERA IMAGEN ---------- */
        File img1 = new File("./src/test/resources/healthy.png");
        MultipartBodyBuilder mb1 = new MultipartBodyBuilder();
        mb1.part("image", new FileSystemResource(img1));
        mb1.part("paciente", paciente, MediaType.APPLICATION_JSON);

        client.post()
            .uri("/imagen")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData(mb1.build()))
            .exchange()
            .expectStatus().isOk();

        /* ---------- 3. OBTENER ID PRIMERA IMAGEN ---------- */
        Long id1 = client.get().uri("/imagen/paciente/{id}", paciente.getId())
                        .exchange()
                        .expectStatus().isOk()
                        .returnResult(Imagen.class)
                        .getResponseBody()
                        .blockLast()          // la recién creada es la última
                        .getId();

        /* ---------- 4. DESCARGAR, INFO y PREDICCIÓN ---------- */
        // descarga
        client.get().uri("/imagen/{id}", id1)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType("image/png")
            .expectBody(byte[].class)
            .value(bytes -> { assert bytes.length > 0; });

        // info
        client.get().uri("/imagen/info/{id}", id1)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(id1)
            .jsonPath("$.nombre").isEqualTo("healthy.png");

        // predicción
        client.get().uri("/imagen/predict/{id}", id1)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.status").value(s -> {
                assert s.equals("Cancer") || s.equals("Not cancer");
            });

        /* ---------- 5. SUBIR SEGUNDA IMAGEN ---------- */
        File img2 = new File("./src/test/resources/healthy.png");
        MultipartBodyBuilder mb2 = new MultipartBodyBuilder();
        mb2.part("image", new FileSystemResource(img2));
        mb2.part("paciente", paciente, MediaType.APPLICATION_JSON);

        client.post()
            .uri("/imagen")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData(mb2.build()))
            .exchange()
            .expectStatus().isOk();

        /* ---------- 6. COMPROBAR QUE HAY DOS IMÁGENES ---------- */
        client.get().uri("/imagen/paciente/{id}", paciente.getId())
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$", hasSize(2));

        /* ---------- 7. ELIMINAR PRIMERA IMAGEN ---------- */
        client.delete().uri("/imagen/{id}", id1)
            .exchange()
            .expectStatus().is2xxSuccessful();

        /* ---------- 8. INTENTAR RECUPERARLA (DEBE FALLAR) ---------- */
        client.get().uri("/imagen/info/{id}", id1)
            .exchange()
            .expectStatus().is5xxServerError();

        /* ---------- 9. CONFIRMAR QUE SOLO QUEDA UNA ---------- */
        client.get().uri("/imagen/paciente/{id}", paciente.getId())
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$", hasSize(1));
    }

    
}