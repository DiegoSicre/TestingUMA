package clubdeportivo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClubDeportivoTest {
    ClubDeportivo club;
    @BeforeEach
    public void setUp() throws  ClubException{
        club = new ClubDeportivo("Club");
    }

    @Test
    @DisplayName("Constructor de ClubDeportivo erroneo por numero de grupos 0")
    public void constructorDeClubDeportivoErroneoNumGruposCero() {
        assertThrows(ClubException.class,()->{ClubDeportivo temp = new ClubDeportivo("club",0);});
    }

    @Test
    @DisplayName("Constructor de ClubDeportivo erroneo por numero de grupos negativo")
    public void constructorDeClubDeportivoErroneoNumGruposNegativo() {
        assertThrows(ClubException.class,()->{ClubDeportivo temp = new ClubDeportivo("club",-1);});
    }

    @Test
    @DisplayName("Constructor correcto con un unico parametro String")
    public void constructorCorrectoUnParametro() throws ClubException {
        ClubDeportivo temp = new ClubDeportivo("Club");
        assertNotNull(club);
    }

    @Test
    @DisplayName("Constructor correcto con todos los parametros")
    public void constructorCorrectoConTodosLosParametros() throws ClubException {
        ClubDeportivo temp = new ClubDeportivo("Club",2);
        assertNotNull(club);
    }

    @Test
    @DisplayName("Anyadir actividad con parametro String incorrecto")
    public void anyadirActividadConParametroStringIncorrecto() throws ClubException {
        String[] data = {"001","mates","4","a","1"};
        assertThrows(ClubException.class,()->{club.anyadirActividad(data);});
    }

    @Test
    @DisplayName("Anyadir actividad con parametro String correcto")
    public void anyadirActividadConParametroStringCorrecto(){
        String[] data = {"001","mates","4","1","1"};
        try{
            club.anyadirActividad(data);
        }catch(ClubException c){
            c.printStackTrace();
        }
    }

    @Test
    @DisplayName("AnyadirActividad lanza excepcion por grupo nulo")
    public void anyadirActividadLanzaExcepcionPorGrupoNulo(){
        Grupo g = null;
        assertThrows(ClubException.class,()->{club.anyadirActividad(g);});
    }

    @Test
    @DisplayName("AnyadirActividad añade un grupo nuevo")
    public void anyadirActividadAnyadeGrupoNuevo() throws ClubException {
        Grupo g = new Grupo("001","act",4,2,3);
        assertDoesNotThrow(()->{club.anyadirActividad(g);});
    }

    @Test
    @DisplayName("AnyadirActividad añade plazas a una actividad existente")
    public void anyadirActividadAnyadeActividadExistente() throws ClubException {
        Grupo g = new Grupo("001","act",4,2,3);
        club.anyadirActividad(g);
        //Aqui ya tenemos las 4 plazas en la estructura de datos
        Grupo temp = new Grupo("001","act",8,2,3);
        club.anyadirActividad(g);
        //Aqui tendriamos las 8 nuevas plazas
        assertEquals(8,temp.getPlazas());
    }

    @Test
    @DisplayName("Plazas libres con una actividad que no existe devuelve 0")
    public void plazasLibresNoExisteDeActividadDevuelveCero() throws ClubException {
        String actividad = "lengua";
        int errorCode = 0;
        Grupo g = new Grupo("001","mates",4,2,3);
        club.anyadirActividad(g);
        assertEquals(errorCode,club.plazasLibres(actividad));
    }

    @Test
    @DisplayName("Plazas libres devuelve el numero correcto de plazas")
    public void plazasLibresDevuelveElNumeroCorrectoDePlazas() throws ClubException {
        String actividad = "lengua";
        Grupo g = new Grupo("001","lengua",4,3,2);
        club.anyadirActividad(g);
        int plazasEsperadas = g.plazasLibres();
        assertEquals(plazasEsperadas,club.plazasLibres(actividad));
    }
}
