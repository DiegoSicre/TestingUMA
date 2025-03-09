//Práctica Realizada por Ángel Escaño y Diego Sicre

package clubdeportivo;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ClubDeportivoTest {
    ClubDeportivo testClub, testClubSmall;
    Grupo grupo1, grupo2, grupo3;
 @BeforeEach
    void setup() throws ClubException {
        testClubSmall = new ClubDeportivo("Club Deportivo Ejemplo", 2);
        testClub = new ClubDeportivo("Club Deportivo Ejemplo", 5);
        grupo1 = new Grupo("Grupo A", "Fútbol", 20, 10, 50.0); // 10 plazas libres
        grupo2 = new Grupo("Grupo B", "Baloncesto", 15, 5, 30.0); // 10 plazas libres
        grupo3 = new Grupo("Grupo C", "Fútbol", 25, 20, 60.0); // 5 plazas libres
        testClub.anyadirActividad(grupo1);
        testClub.anyadirActividad(grupo2);
        testClub.anyadirActividad(grupo3);}
    @Test
    @DisplayName("TestCreacionCorrecta: El club se crea con los valores correctos")
    void clubDeportivo_validInput_createsCorrectly() {
        //Act, Assert
        assertDoesNotThrow(() -> new ClubDeportivo("Club Deportivo Ejemplo", 5));
    }

   
    @Test
    @DisplayName("TestNombreInvalido: No permite nombres nulos o vacíos")
    void clubDeportivo_invalidName_throwsException() {
        //Act, Assert
        assertThrows(ClubException.class, () -> new ClubDeportivo(null));
        assertThrows(ClubException.class, () -> new ClubDeportivo(""));
        assertThrows(ClubException.class, () -> new ClubDeportivo(" "));
    }

    
    @Test
    @DisplayName("TestNumeroGruposInvalido: No permite número de grupos 0 o negativo")
    void clubDeportivo_invalidGroupNumber_throwsException() {
        assertThrows(ClubException.class, () -> new ClubDeportivo("Club Test", 0));
        assertThrows(ClubException.class, () -> new ClubDeportivo("Club Test", -3));
    }

    
    @Test
    @DisplayName("TestConstructorDefault: Usa el tamaño por defecto si no se indica")
    void clubDeportivo_defaultConstructor_usesDefaultSize() {
        assertDoesNotThrow(() -> new ClubDeportivo("Club Default"));
    }
    
    @Test
    @DisplayName("TestAnyadirActividadCorrecta: Añade un grupo sin errores")
    void anyadirActividad_validGroup_addsSuccessfully() throws ClubException {
        assertDoesNotThrow(() -> testClub.anyadirActividad(grupo1));
    }

   
    @Test
    @DisplayName("TestAnyadirActividadNulo: Lanza excepción si el grupo es nulo")
    void anyadirActividad_nullGroup_throwsException() {
        assertThrows(ClubException.class, () -> testClub.anyadirActividad((Grupo) null));
    }

    @Test
    @DisplayName("TestAnyadirActividadDuplicada: No permite duplicados, actualiza plazas")
    void anyadirActividad_duplicateGroup_updatesPlazas() throws ClubException {
        testClub.anyadirActividad(grupo1);
        Grupo updatedGrupo = new Grupo("Grupo A", "Actividad 1", 30, 15, 50.0);
        assertDoesNotThrow(() -> testClub.anyadirActividad(updatedGrupo));
    }

   
    @Test
    @DisplayName("TestAnyadirActividadClubLleno: No permite añadir más grupos de la capacidad máxima")
    void anyadirActividad_clubFull_throwsException() throws ClubException {
        testClubSmall.anyadirActividad(grupo1);
        testClubSmall.anyadirActividad(grupo2);
        Grupo extraGrupo = new Grupo("Grupo C", "Actividad 3", 10, 5, 40.0);
        assertThrows(ClubException.class, () -> testClubSmall.anyadirActividad(extraGrupo));
    }

   
    @Test
    @DisplayName("TestAnyadirActividadDatosNulos: Lanza excepción si los datos son nulos")
    void anyadirActividad_nullData_throwsException() {
        String[] datos = null;
        Grupo g = null;
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(datos));
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(g));
    }

    
    @Test
    @DisplayName("TestAnyadirActividadDatosIncompletos: Lanza excepción si los datos son insuficientes")
    void anyadirActividad_incompleteData_throwsException() {
        String[] datosIncompletos = {"Grupo C", "Actividad 3", "10"};
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(datosIncompletos));
    }

    @Test
    @DisplayName("TestAnyadirActividadFormatoIncorrecto: Lanza excepción si los datos numéricos son incorrectos")
    void anyadirActividad_invalidNumberFormat_throwsException() {
        String[] datosInvalidos = {"Grupo C", "Actividad 3", "diez", "cinco", "cuarenta"};
        String[] datosInvalidos1 = {"Grupo C", "Actividad 3", "10.5", "?", "40€"}; // Decimales en enteros, caracteres especiales
        String[] datosInvalidos2 = {"Grupo C", "Actividad 3", "  ", "NaN", "-"}; // Espacios en blanco y valores no numéricos

    
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(datosInvalidos));
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(datosInvalidos1));
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(datosInvalidos2));

    }
    @Test
    @DisplayName("TestPlazasLibresActividadExistente: Calcula correctamente las plazas libres de una actividad")
    void plazasLibres_existingActivity_returnsCorrectCount() throws ClubException {
        // Act
        int plazasLibresFutbol = testClub.plazasLibres("Fútbol");

        // Assert
        assertEquals(15, plazasLibresFutbol); // 10 (Grupo A) + 5 (Grupo C)
    }

    
    @Test
    @DisplayName("TestPlazasLibresActividadInexistente: Retorna 0 si la actividad no existe")
    void plazasLibres_nonExistingActivity_returnsZero() throws ClubException {
        // Act
        int plazasLibresTenis = testClub.plazasLibres("Tenis");

        // Assert
        assertEquals(0, plazasLibresTenis);
    }

    
    @Test
    @DisplayName("TestPlazasLibresActividadNula: Lanza excepción si la actividad es nula")
    void plazasLibres_nullActivity_throwsException() {
        //Act, assert
        assertThrows(ClubException.class, () -> testClub.plazasLibres(null));
    }

    
    @Test
    @DisplayName("TestPlazasLibresActividadVacia: Lanza excepción si la actividad es una cadena vacía")
    void plazasLibres_emptyActivity_throwsException() {
        //Act, Assert
        assertThrows(ClubException.class, () -> testClub.plazasLibres(""));
        assertThrows(ClubException.class, () -> testClub.plazasLibres(" "));
    }

}
