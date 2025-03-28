package clubdeportivo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ClubDeportivoAltoRendimientoTest {
    
    ClubDeportivoAltoRendimiento club;
    @BeforeEach
    void setUp() throws ClubException{
        club = new ClubDeportivoAltoRendimiento("Club Elite", 20, 10);
       
    }
    @Test
    @DisplayName("Lanza excepción si el máximo de personas es 0")
    void constructor_ZeroMaxPeople_ThrowsException() {
        // Act & Assert
        assertThrows(ClubException.class, () -> new ClubDeportivoAltoRendimiento("Club Elite", 0, 10));
    }
    @Test
    @DisplayName("Lanza excepción si el máximo de personas es negativo")
    void constructor_NegativeMaxPeople_ThrowsException() {
        // Act & Assert
        assertThrows(ClubException.class, () -> new ClubDeportivoAltoRendimiento("Club Elite", -5, 10));
    }
    @Test
    @DisplayName("Lanza excepción si el incremento es 0")
    void constructor_ZeroIncrement_ThrowsException() {
        // Act & Assert
        assertThrows(ClubException.class, () -> new ClubDeportivoAltoRendimiento("Club Elite", 20, 0));
    }
@Test
@DisplayName("Ajusta correctamente el número de plazas al máximo permitido")
void anyadirActividad_ExceedsMaxPlazas_AdjustsToMax() throws ClubException {
    // Arrange
    String[] datos = {"Grupo Elite", "Natación", "50", "10", "30.0"}; // 50 plazas, supera el límite de 20 en este caso.
    
    // Act
    club.anyadirActividad(datos);
    
    // Assert
    String result = club.toString();
    assertTrue(result.contains("P:20"), "El número de plazas no se ajustó correctamente al máximo permitido.");
}

@Test
@DisplayName("No modifica plazas cuando el valor está dentro del límite permitido")
void anyadirActividad_WithinLimit_DoesNotModifyPlazas() throws ClubException {
    // Arrange: maximoPersonasGrupo = 20, adding 15 should be fine
    String[] datos = {"Grupo Elite", "Natación", "15", "5", "30.0"};

    // Act
    club.anyadirActividad(datos);

    // Assert
    String result = club.toString();
    assertTrue(result.contains("P:15"), "El número de plazas debería mantenerse en 15 y no ser modificado.");
}

    @Test
    @DisplayName("Lanza excepción si el incremento es negativo")
    void constructor_NegativeIncrement_ThrowsException() {
        // Act & Assert
        assertThrows(ClubException.class, () -> new ClubDeportivoAltoRendimiento("Club Elite", 20, -5));
    }

    @Test
    @DisplayName("Crea correctamente un club con valores válidos y refleja los valores en toString")
    void constructor_ValidValues_CreatesSuccessfully() throws ClubException {
        // Arrange
        ClubDeportivoAltoRendimiento club = new ClubDeportivoAltoRendimiento("Club Elite", 20, 10);

        // Act
        String result = club.toString();

        // Assert
        assertTrue(result.contains("Club Elite --> [  ]"), "El método toString no incluye el nombre del club.");
     }

    @Test
    @DisplayName("Crea correctamente un club con valores válidos usando el segundo constructor")
    void constructor_ValidValuesWithSize_CreatesSuccessfully() throws ClubException {
        // Arrange
        ClubDeportivo club2 = new ClubDeportivoAltoRendimiento("Club Elite", 5, 20, 10);    
        
        // Act
        String result = club2.toString();

           // Assert
        assertTrue(result.contains("Club Elite --> [  ]"), "El método toString no incluye el nombre del club.");
        
    }



    @Test
    @DisplayName("Lanza excepción si el máximo de personas es 0 usando el segundo constructor")
    void constructor_ZeroMaxPeopleWithSize_ThrowsException() {
        // Act & Assert
        assertThrows(ClubException.class, () -> new ClubDeportivoAltoRendimiento("Club Elite", 5, 0, 10));
    }

    @Test
    @DisplayName("Lanza excepción si el máximo de personas es negativo usando el segundo constructor")
    void constructor_NegativeMaxPeopleWithSize_ThrowsException() {
        // Act & Assert
        assertThrows(ClubException.class, () -> new ClubDeportivoAltoRendimiento("Club Elite", 5, -10, 10));
    }

    @Test
    @DisplayName("Lanza excepción si el incremento es 0 usando el segundo constructor")
    void constructor_ZeroIncrementWithSize_ThrowsException() {
        // Act & Assert
        assertThrows(ClubException.class, () -> new ClubDeportivoAltoRendimiento("Club Elite", 5, 20, 0));
    }

    @Test
    @DisplayName("Lanza excepción si el incremento es negativo usando el segundo constructor")
    void constructor_NegativeIncrementWithSize_ThrowsException() {
        // Act & Assert
        assertThrows(ClubException.class, () -> new ClubDeportivoAltoRendimiento("Club Elite", 5, 20, -5));
    }
   
    @Test
    @DisplayName("Lanza excepción si las plazas son mayores al máximo y contienen caracteres especiales")
    void anyadirActividad_ExceedsMaxPlazas_SpecialChars_ThrowsException() {
        // Arrange
        String[] datos = {"Grupo A", "Natación", "50+", "5", "40.0"}; // Plazas con caracteres especiales
        
        // Act & Assert
        assertThrows(ClubException.class, () -> club.anyadirActividad(datos), 
            "Debe lanzar excepción por caracteres especiales en plazas.");
    }

    @Test
    @DisplayName("Lanza excepción por haber menos datos de los requeridos")
    void anyadirActividad_lessThan5CellDataArray_ThrowsException() {
        // Arrange
        String[] datos = {"Grupo A", "Natación", "50", "5", }; // Plazas con caracteres especiales
        
        // Act & Assert
        assertThrows(ClubException.class, () -> club.anyadirActividad(datos), 
            "Debe lanzar excepción por caracteres especiales en plazas.");
    }

    @Test
    @DisplayName("Lanza excepción si la tarifa es negativa y las plazas exceden el máximo permitido")
    void anyadirActividad_ExceedsMaxPlazas_NegativeTarifa_ThrowsException() {
        // Arrange
        String[] datos = {"Grupo A", "Natación", "50", "5", "-40.0"}; // Tarifa negativa
        
        // Act & Assert
        assertThrows(ClubException.class, () -> club.anyadirActividad(datos), 
            "Debe lanzar excepción por tarifa negativa.");
    }
    @Test
    @DisplayName("Establece el número de plazas al máximo permitido si se excede y lo refleja en toString()")
    void anyadirActividad_ExceedsMaxPlazas_UpdatesCorrectly() throws ClubException {
        // Arrange
        String[] datos = {"Grupo A", "Natación", "50", "5", "40.0"}; // 50 plazas, pero el máximo es 20 (definido en setup)
        int expectedPlazas = 20; // maximoPersonasGrupo definido en el constructor del test

        // Act
        club.anyadirActividad(datos);

        // Assert - Se comprueba que se ha limitado correctamente
        String result = club.toString();
        assertTrue(result.contains("Grupo A"), "El grupo no fue añadido correctamente.");
        assertTrue(result.contains("Natación"), "La actividad no se refleja correctamente.");
        assertTrue(result.contains("P:" + expectedPlazas), "Las plazas deberían estar limitadas al máximo permitido.");
        assertTrue(result.contains("M:5"), "El número de matriculados no se refleja correctamente.");
        assertTrue(result.contains("40.0 euros"), "La tarifa no se refleja correctamente.");
    }

    @Test
    @DisplayName("Aplica correctamente el incremento a los ingresos")
    void ingresos_AppliesIncrementCorrectly() throws ClubException {
        // Arrange
        Grupo grupo1 = new Grupo("Grupo A", "Natación", 20, 10, 50.0); // 10 matriculados → 10 * 50.0 = 500.0 ingresos base
        Grupo grupo2 = new Grupo("Grupo B", "Baloncesto", 15, 5, 40.0); // 5 matriculados → 5 * 40.0 = 200.0 ingresos base

        double ingresosBaseEsperados = 500.0 + 200.0; // Total sin incremento = 700.0
        double incremento = 10; // Definido en setup (10%)
        double ingresosEsperados = ingresosBaseEsperados + (ingresosBaseEsperados * incremento / 100); // 700 + (700 * 0.10) = 770.0

        // Act
        club.anyadirActividad(grupo1);
        club.anyadirActividad(grupo2);
        double ingresosCalculados = club.ingresos();

        // Assert
        assertEquals(ingresosEsperados, ingresosCalculados, 0.01, "Los ingresos no reflejan correctamente el incremento aplicado.");
    }


}