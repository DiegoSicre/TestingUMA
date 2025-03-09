package clubdeportivo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GrupoTest {
  static Grupo temp;
  @BeforeEach
    public void crearGrupo() throws ClubException {
      temp = new Grupo("001", "mates", 4, 2, 3);
    }
  @Test
    @DisplayName("Grupo da excepcion con nplazas")
    public void constructorExceptionNplazas() throws ClubException {
      assertThrows(ClubException.class, () -> {Grupo temp = new
              Grupo("aux","aux",0,1,1);});
    }

  @Test
  @DisplayName("Grupo da excepcion con matriculados")
  public void constructorExceptionMatriculados() throws ClubException {
    assertThrows(ClubException.class, () -> {Grupo temp = new
            Grupo("aux","aux",1,-1,1);});
  }

  @Test
  @DisplayName("Grupo da excepcion con tarifa")
  public void constructorExceptionTarifa() throws ClubException {
    assertThrows(ClubException.class, () -> {Grupo temp = new
            Grupo("aux","aux",1,1,0);});
  }

  @Test
  @DisplayName("Grupo da excepcion con matriculados>nplazas")
  public void constructorExceptionMatMayorqueNplazas() throws ClubException {
    assertThrows(ClubException.class, () -> {Grupo temp = new
            Grupo("aux","aux",1,2,1);});
  }

  @Test
  @DisplayName("Constructor correcto")
  public void constructorCorrecto() throws ClubException {
    Grupo aux = new Grupo("cod1","aux",1,1,1);
    assertNotNull(aux);
  }

  @Test
  @DisplayName("Plazas libres bien calculadas")
  public void plazasLibresCorrectamenteCalculadas() throws ClubException {
      int resEsperado = temp.getPlazas()-temp.getMatriculados();
      int plazasLibres = temp.plazasLibres();
      assertEquals(plazasLibres,resEsperado);
  }

  @Test
  @DisplayName("Matricular correcto")
  public void matricularCorrecto() throws ClubException {
      int alumnosNuevos = 1;
      int alumnosPrevios = temp.getMatriculados();
      temp.matricular(alumnosNuevos);
      assertEquals(temp.getMatriculados(), alumnosPrevios + alumnosNuevos);
  }

  @Test
  @DisplayName("Alumnos nuevos a matricular == 0")
  public void alumnosNuevosCero(){
    assertThrows(ClubException.class, () -> {temp.matricular(0);});
  }

  @Test
  @DisplayName("Alumnos nuevos a matricular negativos")
  public void alumnosNuevosNegativo(){
    assertThrows(ClubException.class,()->{temp.matricular(-1);});
  }

  @Test
  @DisplayName("Alumnos nuevos a matricular menor que las plazas")
  public void alumnosNuevosMenorQuePlazas(){
    int plazasLibres = temp.plazasLibres();
    int alumnosMatricular = plazasLibres+1;
    assertThrows(ClubException.class,()->{temp.matricular(alumnosMatricular);});
  }

  @Test
  @DisplayName("Actualizar plaza correcto")
  public void actualizarPlazaCorrecto() throws ClubException{
    int nuevaPlaza = 6;
    temp.actualizarPlazas(nuevaPlaza);
    assertEquals(temp.getPlazas(),nuevaPlaza);
  }

  @Test
  @DisplayName("Actualizar plazas a 0")
  public void actualizarPlazaACero(){
    int nuevaPlaza = 0;
    assertThrows(ClubException.class,()->{temp.actualizarPlazas(nuevaPlaza);});
  }

  @Test
  @DisplayName("Actualizar plazas negativa")
  public void actualizarPlazaNegativa(){
    int nuevaPlaza = -1;
    assertThrows(ClubException.class,()->{temp.actualizarPlazas(nuevaPlaza);});
  }

  @Test
  @DisplayName("Actualizar plazas con menos plazas que matriculados")
  public void actualizarPlazaMenorQueMatriculados(){
    int nuevaPlaza = 1;
    assertThrows(ClubException.class,()->{temp.actualizarPlazas(nuevaPlaza);});
  }

  @Test
  @DisplayName("Equals correcto por memoria")
  public void equalsCorrectoMemoria() {
    Grupo aux = temp;
    assertEquals(aux, temp);
  }

  @Test
  @DisplayName("Equals correcto por parametros")
  public void equalsCorrectoParametro() throws ClubException {
    Grupo aux2 = new Grupo("001","Mates",1,1,1);
    assertEquals(aux2,temp);
  }

  @Test
  @DisplayName("Equals falso para dos objetos distintos")
  public void equalsFalso() throws ClubException {
    Grupo aux = new Grupo("002","Mates",1,1,1);
    Grupo aux2 = new Grupo("001","Lengua",1,1,1);
    assertNotEquals(aux,temp);
    assertNotEquals(aux2,temp);
  }

  @Test
  @DisplayName("Equals falso con dos objetos de distintas clases")
  public void equalsFalsoObjetosDistintasClases(){
    String s = "Esto no es un Grupo";
    assertNotEquals(temp, s);
  }

  @Test
  @DisplayName("Hashcode correcto")
  public void hashcodeCorrecto() throws ClubException {
    Grupo aux = new Grupo(temp.getCodigo(),temp.getActividad()
    ,temp.getPlazas(),temp.getMatriculados(),temp.getTarifa());
    assertEquals(aux.hashCode(),temp.hashCode());
  }

  @Test
  @DisplayName("ToString Cadena Correcta")
  public void toStringCadenaCorrecta(){
    String cadenaEsperada = "(001 - mates - 3.0 euros - P:4 - M:2)";
    assertEquals(cadenaEsperada,temp.toString());
  }
}

