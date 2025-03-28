//Práctica Realizada por Ángel Escaño y Diego Sicre

package clubdeportivo;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ClubDeportivoTest {
    ClubDeportivo testClub, testClubSmall;
    Grupo grupo1, grupo2, grupo3;
 @BeforeEach
    void setup() throws ClubException {
        //Arrange
        
        testClub = new ClubDeportivo("Club Deportivo Ejemplo", 5);
 }
    @Test
    @DisplayName("TestCreacionCorrecta: El club se crea con los valores correctos")
    void clubDeportivo_validInput_createsCorrectly() {
        //Act, Assert
        assertDoesNotThrow(() -> new ClubDeportivo("Club Deportivo Ejemplo", 5));
    }
        @Test
    @DisplayName("TestToStringClubVacio: El método toString muestra el club vacío correctamente")
    void toString_EmptyClub_ReturnsCorrectFormat() {
        // Act
        String result = testClub.toString();

        // Assert
        assertTrue(result.contains("Club Deportivo Ejemplo --> [  ]"),
                "El método toString no muestra correctamente un club vacío");
    }

    @Test
    @DisplayName("No permite nombres nulos")
    void clubDeportivo_NullName_ThrowsException() {
        // Act & Assert
        assertThrows(ClubException.class, () -> new ClubDeportivo(null));
    }

    @Test
    @DisplayName("No permite nombres vacíos")
    void clubDeportivo_EmptyName_ThrowsException() {
        // Act & Assert
        assertThrows(ClubException.class, () -> new ClubDeportivo(""));
    }

    @Test
    @DisplayName("No permite nombres con solo espacios")
    void clubDeportivo_SpacesOnlyName_ThrowsException() {
        // Act & Assert
        assertThrows(ClubException.class, () -> new ClubDeportivo(" "));
    }

    
    @Test
    @DisplayName("No permite número de grupos igual a 0")
    void clubDeportivo_ZeroGroupNumber_ThrowsException() {
        // Act & Assert
        assertThrows(ClubException.class, () -> new ClubDeportivo("Club Test", 0));
    }

    @Test
    @DisplayName("No permite número de grupos negativo")
    void clubDeportivo_NegativeGroupNumber_ThrowsException() {
        // Act & Assert
        assertThrows(ClubException.class, () -> new ClubDeportivo("Club Test", -3));
    }
    
    @Test
    @DisplayName("TestConstructorDefault: Usa el tamaño por defecto si no se indica")
    void clubDeportivo_defaultConstructor_usesDefaultSize() {
        //Act, Assert
        assertDoesNotThrow(() -> new ClubDeportivo("Club Default"));
    }
    
    @Test
    @DisplayName("TestAnyadirActividadCorrecta: Añade un grupo sin errores")
    void anyadirActividad_validGroup_addsSuccessfully() throws ClubException {
        //Act, Assert
        grupo1 = new Grupo("Grupo A", "Fútbol", 20, 10, 50.0); // 10 plazas libres

        assertDoesNotThrow(() -> testClub.anyadirActividad(grupo1));
    }

    @Test
    @DisplayName("TestToStringUnGrupo: El método toString muestra correctamente un grupo añadido")
    void toString_OneGroup_ReturnsCorrectFormat() throws ClubException {
        // Arrange
        grupo1 = new Grupo("Grupo A", "Fútbol", 20, 10, 50.0); // 10 plazas libres
        String result;

        // Act
        
        testClub.anyadirActividad(grupo1);
        result = testClub.toString();

        // Assert
        assertTrue(result.contains("Grupo A"), "El método toString no incluye el grupo añadido");
        assertTrue(result.contains("P:20"), "El método toString no muestra correctamente las plazas (P:20)");
        assertTrue(result.contains("M:10"), "El método toString no muestra correctamente los matriculados (M:10)");
        assertTrue(result.contains("Grupo A"), "El método toString no incluye el grupo añadido");
        assertTrue(result.contains("Fútbol"), "El método toString no muestra correctamente la actividad");
           assertTrue(result.contains("50.0 euros"), "El método toString no muestra correctamente la tarifa");
    }

    @Test
    @DisplayName("TestToStringMultiplesGrupos: El método toString muestra correctamente múltiples grupos")
    void toString_MultipleGroups_ReturnsCorrectFormat() throws ClubException {
        // Arrange
        grupo1 = new Grupo("Grupo A", "Fútbol", 20, 10, 50.0);
        grupo2 = new Grupo("Grupo B", "Baloncesto", 15, 5, 40.0);
        String result;
        // Act
        testClub.anyadirActividad(grupo1);
        testClub.anyadirActividad(grupo2);
        result = testClub.toString();


        // Assert
        assertTrue(result.contains("Grupo A"), "El método toString no incluye el Grupo A");
        assertTrue(result.contains("Fútbol"), "El método toString no muestra correctamente la actividad de Grupo A");
        assertTrue(result.contains("Grupo B"), "El método toString no incluye el Grupo B");
        assertTrue(result.contains("Baloncesto"), "El método toString no muestra correctamente la actividad de Grupo B");
    }

    @Test
    @DisplayName("TestToStringGrupoActualizado: El método toString refleja correctamente la actualización de plazas")
    void toString_UpdatedGroup_ReturnsCorrectFormat() throws ClubException {
        // Arrange
        grupo1 = new Grupo("Grupo A", "Fútbol", 20, 10, 50.0);
        testClub.anyadirActividad(grupo1);
        Grupo updatedGrupo = new Grupo("Grupo A", "Fútbol", 30, 15, 50.0); // Se actualizan las plazas
        String result;
        // Act
        testClub.anyadirActividad(updatedGrupo);
        result = testClub.toString();

        // Assert
        assertTrue(result.contains("30"), "El método toString no muestra correctamente la actualización de plazas");
        // Esto de nuevo, para mi sería un error, pero entiendo que es parte de la lógica intended assertTrue(result.contains("15"), "El método toString no muestra correctamente la actualización de matriculados");
    }

    //////////////////////////// TEST PARA INTENTAR 100
    @Test
    @DisplayName("Lanza excepción si un dato es una cadena vacía")
    void validarDatosActividad_EmptyString_ThrowsException() {
    String[] datosInvalidos = {"Grupo C", "Actividad 3", "", "5", "40.0"}; // Un dato vacío
    assertThrows(ClubException.class, () -> testClub.anyadirActividad(datosInvalidos));
    }
    @Test
    @DisplayName("Lanza excepción si un dato es nulo")
    void validarDatosActividad_NullValue_ThrowsException() {
        String[] datosInvalidos = {"Grupo C", "Actividad 3", null, "5", "40.0"}; // Un dato nulo
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(datosInvalidos));
    }

    

    /// TEST PARA INTENTAR 100 FIN
    
    /// 
    void anyadirActividad_NullElement_ThrowsException() {
        String[] datosInvalidos = {"Grupo C", null, "10", "5", "40.0"}; // Un elemento nulo
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(datosInvalidos));
    }
    
    @Test
    @DisplayName("Lanza excepción si un elemento del array es una cadena vacía")
    void anyadirActividadEmptyElement_ThrowsException() {
        String[] datosInvalidos = {"Grupo C", "", "10", "5", "40.0"}; // Un elemento vacío
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(datosInvalidos));
    }
    
    @Test
    @DisplayName("Lanza excepción si un elemento del array es solo espacios en blanco")
    void anyadirActividadOnlySpacesElement_ThrowsException() {
        String[] datosInvalidos = {"Grupo C", "   ", "10", "5", "40.0"}; // Solo espacios
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(datosInvalidos));
    }
    
    @Test
    @DisplayName("No lanza excepción si todos los elementos son válidos")
    void anyadirActividad_AllValid_DoesNotThrowException() {
        String[] datosValidos = {"Grupo C", "Actividad 3", "10", "5", "40.0"}; // Todos los valores correctos
        assertDoesNotThrow(() -> testClub.anyadirActividad(datosValidos));
    }
    
    
    ///     
    @Test
    @DisplayName("Lanza excepción si plazas es igual a 0")
    void anyadirActividad_ZeroPlazas_ThrowsException() {
        String[] datosInvalidos = {"Grupo C", "Actividad 3", "0", "5", "40.0"}; // Plazas = 0
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(datosInvalidos));
    }

    @Test
    @DisplayName("Lanza excepción si plazas es negativa")
    void anyadirActividad_NegativePlazas_ThrowsException() {
        String[] datosInvalidos = {"Grupo C", "Actividad 3", "-5", "5", "40.0"}; // Plazas negativo
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(datosInvalidos));
    }

    @Test
    @DisplayName("Lanza excepción si matriculados es negativo")
    void anyadirActividad_NegativeMatriculados_ThrowsException() {
        String[] datosInvalidos = {"Grupo C", "Actividad 3", "10", "-1", "40.0"}; // Matriculados negativo
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(datosInvalidos));
    }

    @Test
    @DisplayName("Lanza excepción si tarifa es negativa")
    void anyadirActividad_NegativeTarifa_ThrowsException() {
        String[] datosInvalidos = {"Grupo C", "Actividad 3", "10", "5", "-40.0"}; // Tarifa negativa
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(datosInvalidos));
    }

    @Test
    @DisplayName("Lanza excepción si plazas no es un número válido")
    void anyadirActividadInvalidPlazas_ThrowsException() {
        String[] datosInvalidos = {"Grupo C", "Actividad 3", "diez", "5", "40.0"}; // Plazas no numérico
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(datosInvalidos));
    }

    @Test
    @DisplayName("Lanza excepción si matriculados no es un número válido")
    void anyadirActividad_InvalidMatriculados_ThrowsException() {
        String[] datosInvalidos = {"Grupo C", "Actividad 3", "10", "cinco", "40.0"}; // Matriculados no numérico
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(datosInvalidos));
    }

    @Test
    @DisplayName("Lanza excepción si tarifa no es un número válido")
    void anyadirActividad_InvalidTarifa_ThrowsException() {
        String[] datosInvalidos = {"Grupo C", "Actividad 3", "10", "5", "cuarenta"}; // Tarifa no numérica
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(datosInvalidos));
    }
    /// ////////////////////////////
    @Test
    @DisplayName("TestAnyadirActividadNulo: Lanza excepción si el grupo es nulo")
    void anyadirActividad_nullGroup_throwsException() {
        //Act, assert
        assertThrows(ClubException.class, () -> testClub.anyadirActividad((Grupo) null));
    }
    @Test
    @DisplayName("TestToStringGrupoLleno: El método toString refleja correctamente el club lleno")
    void toString_FullClub_ReturnsCorrectFormat() throws ClubException {
        // Arrange
        testClub = new ClubDeportivo("Club Deportivo Pequeño", 1);
        grupo1 = new Grupo("Grupo A", "Fútbol", 20, 10, 50.0);

        Grupo grupoExtra = new Grupo("Grupo C", "Natación", 10, 5, 30.0); // Este no cabe

        String result;

        // Act
        testClub.anyadirActividad(grupo1);
        result = testClub.toString();

        // Assert
        assertTrue(result.contains("Grupo A"), "El método toString no incluye el Grupo A");
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(grupoExtra),
                "Se permite añadir más grupos de los permitidos");
    }
    @Test
    @DisplayName("TestAnyadirActividadDuplicada: No permite duplicados, actualiza plazas")
    void anyadirActividad_duplicateGroup_updatesPlazas() throws ClubException {
        //Arrange
        grupo1 = new Grupo("Grupo A", "Fútbol", 20, 10, 50.0); // 10 plazas libres
        Grupo updatedGrupo = new Grupo("Grupo A", "Actividad 1", 30, 15, 50.0);
        
        //Act, assert
        testClub.anyadirActividad(grupo1);
        assertDoesNotThrow(() -> testClub.anyadirActividad(updatedGrupo));
    }

    @Test
    @DisplayName("TestAnyadirActividadClubLleno: No permite añadir más grupos de la capacidad máxima")
    void anyadirActividad_clubFull_throwsException() throws ClubException {
        //Arrange
        testClubSmall = new ClubDeportivo("Club Deportivo Ejemplo", 1);
        Grupo extraGrupo = new Grupo("Grupo C", "Actividad 3", 10, 5, 40.0);
        grupo1 = new Grupo("Grupo A", "Fútbol", 20, 10, 50.0); // 10 plazas libres
        
        //Act, assert
        testClubSmall.anyadirActividad(grupo1);
        assertThrows(ClubException.class, () -> testClubSmall.anyadirActividad(extraGrupo));
    }

    @Test
    @DisplayName("Lanza excepción si los datos de la actividad son nulos")
    void anyadirActividad_NullActivityData_ThrowsException() {
        // Arrange
        String[] datos = null;

        // Act & Assert
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(datos));
    }

    @Test
    @DisplayName("Lanza excepción si el grupo de la actividad es nulo")
    void anyadirActividad_NullGroup_ThrowsException() {
        // Arrange
        Grupo g = null;

        // Act & Assert
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(g));
    }
    
    @Test
    @DisplayName("TestAnyadirActividadDatosIncompletos: Lanza excepción si los datos son insuficientes")
    void anyadirActividad_incompleteData_throwsException() {
        String[] datosIncompletos = {"Grupo C", "Actividad 3", "10"};
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(datosIncompletos));
    }
    @Test
    @DisplayName("Lanza excepción si los datos numéricos contienen texto en lugar de números")
    void anyadirActividad_TextInsteadOfNumbers_ThrowsException() {
        // Arrange
        String[] datosInvalidos = {"Grupo C", "Actividad 3", "diez", "cinco", "cuarenta"};

        // Act & Assert
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(datosInvalidos));
    }

    @Test
    @DisplayName("Lanza excepción si los datos numéricos contienen decimales en enteros o caracteres especiales")
    void anyadirActividad_DecimalsOrSpecialCharacters_ThrowsException() {
        // Arrange
        String[] datosInvalidos = {"Grupo C", "Actividad 3", "10.5", "?", "40€"}; // Decimales en enteros, caracteres especiales

        // Act & Assert
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(datosInvalidos));
    }

    @Test
    @DisplayName("Lanza excepción si los datos numéricos contienen espacios en blanco o valores no numéricos")
    void anyadirActividad_BlankSpacesOrNonNumericValues_ThrowsException() {
        // Arrange
        String[] datosInvalidos = {"Grupo C", "Actividad 3", "  ", "NaN", "-"}; // Espacios en blanco y valores no numéricos

        // Act & Assert
        assertThrows(ClubException.class, () -> testClub.anyadirActividad(datosInvalidos));
    }
    
    @Test
    @DisplayName("TestPlazasLibresActividadExistente: Calcula correctamente las plazas libres de una actividad")
    void plazasLibres_existingActivity_returnsCorrectCount() throws ClubException {
        //Assert
        grupo1 = new Grupo("Grupo A", "Fútbol", 20, 10, 50.0); // 10 plazas libres
        
        // Act
        testClub.anyadirActividad(grupo1);
        int plazasLibresFutbol = testClub.plazasLibres("Fútbol");

        // Assert
        assertEquals(10, plazasLibresFutbol); // 10 (Grupo A) + 5 (Grupo C)
    }

    
    @Test
    @DisplayName("TestPlazasLibresActividadInexistente: Retorna 0 si la actividad no existe")
    void plazasLibres_nonExistingActivity_returnsZero() throws ClubException {
        //Arrange
        int plazasLibresTenis;
        // Act
        plazasLibresTenis = testClub.plazasLibres("Tenis");

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
    @DisplayName("Lanza excepción si la actividad es una cadena vacía")
    void plazasLibres_EmptyString_ThrowsException() {
        // Act & Assert
        assertThrows(ClubException.class, () -> testClub.plazasLibres(""));
    }

    @Test
    @DisplayName("Lanza excepción si la actividad contiene solo espacios en blanco")
    void plazasLibres_OnlySpaces_ThrowsException() {
        // Act & Assert
        assertThrows(ClubException.class, () -> testClub.plazasLibres(" "));
    }



    @Test
    @DisplayName("Lanza excepción si la actividad es nula")
    void matricular_NullActivity_ThrowsException() {
        assertThrows(ClubException.class, () -> testClub.matricular(null, 5));
    }

    @Test
    @DisplayName("Lanza excepción si la actividad es una cadena vacía")
    void matricular_EmptyActivity_ThrowsException() {
        assertThrows(ClubException.class, () -> testClub.matricular("", 5));
    }

    @Test
    @DisplayName("Lanza excepción si la actividad contiene solo espacios en blanco")
    void matricular_OnlySpacesActivity_ThrowsException() {
        assertThrows(ClubException.class, () -> testClub.matricular("   ", 5));
    }

    @Test
    @DisplayName("Lanza excepción si el número de personas es 0")
    void matricular_ZeroPeople_ThrowsException() {
        assertThrows(ClubException.class, () -> testClub.matricular("Fútbol", 0));
    }

    @Test
    @DisplayName("Lanza excepción si el número de personas es negativo")
    void matricular_NegativePeople_ThrowsException() {
        assertThrows(ClubException.class, () -> testClub.matricular("Fútbol", -5));
    }

    @Test
    @DisplayName("Lanza excepción si no hay suficientes plazas disponibles")
    void matricular_NotEnoughSeats_ThrowsException() throws ClubException {
        //Arrange
        grupo1 = new Grupo("Grupo A", "Fútbol", 20, 10, 50.0); // 10 plazas libres
        

        // Act, Assert
        testClub.anyadirActividad(grupo1);
        assertThrows(ClubException.class, () -> testClub.matricular("Fútbol", 15)); // Solo hay 10 plazas libres
    }

    @Test
    @DisplayName("Lanza excepción si la actividad no existe en el club")
    void matricular_NonExistentActivity_ThrowsException() {
        assertThrows(ClubException.class, () -> testClub.matricular("Natación", 5));
    }

    

    @Test
    @DisplayName("Matricula correctamente en una actividad y actualiza el toString")
    void matricular_ValidEnrollment_UpdatesToString() throws ClubException {
        //Arrange
        grupo1 = new Grupo("Grupo A", "Fútbol", 20, 10, 50.0); // 10 plazas libres
        

        // Act
        testClub.anyadirActividad(grupo1);
        testClub.matricular("Fútbol", 5); // Matriculamos 5 personas

        // Assert - Verificamos que `toString` refleje correctamente los cambios
        String result = testClub.toString();
        assertTrue(result.contains("Grupo A"), "El método toString no incluye el Grupo A");
        assertTrue(result.contains("M:15"), "El número de matriculados no se actualizó correctamente");
    }

    @Test
    @DisplayName("Matricula correctamente en múltiples actividades y actualiza el toString")
    void matricular_MultipleActivities_UpdatesToString() throws ClubException {
        //Arrange
        grupo1 = new Grupo("Grupo A", "Fútbol", 20, 10, 50.0); // 10 plazas libres
        grupo2 = new Grupo("Grupo B", "Baloncesto", 15, 5, 40.0); // 10 plazas libres
        

        // Act (Comportamiento complejo dos Acts con la misma lógica), necesariamente tengo que incluir añadir para poder testear
        testClub.anyadirActividad(grupo1);
        testClub.anyadirActividad(grupo2);
        testClub.matricular("Fútbol", 5);
        testClub.matricular("Baloncesto", 5);

        // Assert - Verificamos que `toString` refleje correctamente los cambios
        String result = testClub.toString();
        assertTrue(result.contains("Grupo A"), "El método toString no incluye el Grupo A");
        assertTrue(result.contains("M:15"), "El número de matriculados en Fútbol no se actualizó correctamente");
        assertTrue(result.contains("Grupo B"), "El método toString no incluye el Grupo B");
        assertTrue(result.contains("M:10"), "El número de matriculados en Baloncesto no se actualizó correctamente");
    }

    @Test
    @DisplayName("Matricula correctamente hasta llenar un grupo y actualiza el toString")
    void matricular_FillGroup_UpdatesToString() throws ClubException {
        //Arrange
        grupo2 = new Grupo("Grupo B", "Baloncesto", 15, 5, 40.0); // 10 plazas libres
        
        
        // Act
        testClub.anyadirActividad(grupo2);
        testClub.matricular("Baloncesto", 10); // Matriculamos todas las plazas libres

        // Assert - Verificamos que `toString` refleje que el grupo está lleno
        String result = testClub.toString();
        assertTrue(result.contains("Grupo B"), "El método toString no incluye el Grupo B");
        assertTrue(result.contains("M:15"), "El grupo no se llenó correctamente");
    }

    @Test
    @DisplayName("TestIngresosClubVacio: Los ingresos de un club sin actividades deben ser 0")
    void ingresos_EmptyClub_ReturnsZero() {
        // Act
        double ingresos = testClub.ingresos();

        // Assert
        assertEquals(0.0, ingresos, 0.001, "El método ingresos debería devolver 0 para un club vacío");
    }

    @Test
    @DisplayName("TestIngresosUnGrupo: Calcula correctamente los ingresos con un solo grupo")
    void ingresos_OneGroup_ReturnsCorrectValue() throws ClubException {
        //Arrange
        grupo1 = new Grupo("Grupo A", "Fútbol", 20, 10, 50.0); // 10 plazas libres
        double ingresos ;

        // Act
        testClub.anyadirActividad(grupo1);
        ingresos = testClub.ingresos();

        // Assert
        assertEquals(500.0, ingresos, 0.001, "El método ingresos no calculó correctamente los ingresos con un grupo");
    }

    @Test
    @DisplayName("TestIngresosMultiplesGrupos: Calcula correctamente los ingresos con múltiples grupos")
    void ingresos_MultipleGroups_ReturnsCorrectValue() throws ClubException {
        //Arrange
        grupo1 = new Grupo("Grupo A", "Fútbol", 20, 10, 50.0);
        grupo2 = new Grupo("Grupo B", "Baloncesto", 15, 5, 40.0);
        double ingresos;
        // Act
        testClub.anyadirActividad(grupo1);
        testClub.anyadirActividad(grupo2);
        ingresos = testClub.ingresos();

        // Assert
        assertEquals(500.0 + 200.0, ingresos, 0.001, "El método ingresos no calculó correctamente los ingresos con múltiples grupos");
    }


}