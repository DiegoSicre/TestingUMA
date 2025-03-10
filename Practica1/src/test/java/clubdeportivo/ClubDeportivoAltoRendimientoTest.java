package clubdeportivo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClubDeportivoAltoRendimientoTest {
    ClubDeportivoAltoRendimiento club;
    @BeforeEach
    public void setUp() throws ClubException {
        club = new ClubDeportivoAltoRendimiento(
                "Club",10,5
        );
    }
    @Test
    @DisplayName("Constructor de Club deportivo de alto rendimiento(Sin tam) correcto")
    public void constructorDeClubDeportivoAltoRendimientoSinTamCorrecto() throws ClubException {
        String nombre = "ClubDeportivoAltoRendimiento";
        int maximo = 10;
        int incremento = 2;
        ClubDeportivoAltoRendimiento temp = null;
        temp = new ClubDeportivoAltoRendimiento(nombre,maximo,incremento);
        assertNotNull(temp);
    }

    @Test
    @DisplayName("Constructor de Club deportivo de alto rendimiento(Sin tam) falla por maximo menor que 0")
    public void constructorDeClubDeportivoAltoRendimientoSinTamFallaMaximoMenorQueCero() throws ClubException {
        String nombre = "ClubDeportivoAltoRendimiento";
        int maximo = 0;
        int incremento = 2;
        assertThrows(ClubException.class, () -> {ClubDeportivoAltoRendimiento temp = new ClubDeportivoAltoRendimiento(nombre,maximo,incremento);});
    }

    @Test
    @DisplayName("Constructor de Club deportivo de alto rendimiento(Sin tam) falla por incremento menor que 0")
    public void constructorDeClubDeportivoAltoRendimientoSinTamFallaIncrementoMenorQueCero() throws ClubException {
        String nombre = "ClubDeportivoAltoRendimiento";
        int maximo = 10;
        int incremento = 0;
        assertThrows(ClubException.class, () -> {ClubDeportivoAltoRendimiento temp = new ClubDeportivoAltoRendimiento(nombre,maximo,incremento);});
    }

    @Test
    @DisplayName("Constructor de Club deportivo de alto rendimiento(Con tam) correcto")
    public void constructorDeClubDeportivoAltoRendimientoConTamCorrecto() throws ClubException {
        String nombre = "ClubDeportivoAltoRendimiento";
        int tam = 5;
        int maximo = 10;
        int incremento = 2;
        ClubDeportivoAltoRendimiento temp = null;
        temp = new ClubDeportivoAltoRendimiento(nombre,tam,maximo,incremento);
        assertNotNull(temp);
    }

    @Test
    @DisplayName("Constructor de Club deportivo de alto rendimiento(Con tam) falla por maximo menor que 0")
    public void constructorDeClubDeportivoAltoRendimientoConTamFallaMaximoMenorQueCero() throws ClubException {
        String nombre = "ClubDeportivoAltoRendimiento";
        int tam = 5;
        int maximo = 0;
        int incremento = 2;
        assertThrows(ClubException.class, () -> {ClubDeportivoAltoRendimiento temp = new ClubDeportivoAltoRendimiento(nombre,tam,maximo,incremento);});
    }

    @Test
    @DisplayName("Constructor de Club deportivo de alto rendimiento(Con tam) falla por incremento menor que 0")
    public void constructorDeClubDeportivoAltoRendimientoConTamFallaIncrementoMenorQueCero() throws ClubException {
        String nombre = "ClubDeportivoAltoRendimiento";
        int tam = 5;
        int maximo = 10;
        int incremento = 0;
        assertThrows(ClubException.class, () -> {ClubDeportivoAltoRendimiento temp = new ClubDeportivoAltoRendimiento(nombre,tam,maximo,incremento);});
    }

    @Test
    @DisplayName("Anyadir actividad falla por dar menos datos de los necesarios")
    public void anyadirActividadFallaPorDarMenosDatosDeLosNecesarios() throws ClubException {
        String[] datos = {"001","mates","3"};
        assertThrows(ClubException.class, () -> {club.anyadirActividad(datos);});
    }

    @Test
    @DisplayName("Anyadir actividad falla por pasar mal los parametros de los datos")
    public void anyadirActividadFallaPorPasarMalLosParametrosDeLosDatosDeLosDatos() throws ClubException {
        String[] datos = {"2","3","mates","1","001"};
        assertThrows(ClubException.class, () -> {club.anyadirActividad(datos);});
    }

    @Test
    @DisplayName("Anyadir actividad funciona correctamente")
    public void anyadirActividadFallaCorrectamente() throws ClubException {
        String[] datos = {"001","mates","3","1","2"};
        assertDoesNotThrow(() -> {club.anyadirActividad(datos);});
    }

    @Test
    @DisplayName("Ingresos calculados correctamente")
    public void ingresosCalculadosCorrectamente() throws ClubException {
        double resultadoEsperado = 2.0+2.0*(5.0/100.0);
        String[] datos = {"001","mates","3","1","2"};
        club.anyadirActividad(datos);
        assertEquals(resultadoEsperado,club.ingresos());
    }
}
