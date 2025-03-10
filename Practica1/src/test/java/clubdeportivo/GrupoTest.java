package clubdeportivo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GrupoTest {
  private Grupo grupo;

  @BeforeEach
  void setUp() throws ClubException {
    grupo = new Grupo("001", "Fútbol", 20, 10, 50.0);
  }

  @Test
  @DisplayName("Lanza excepción si el código del grupo es nulo")
  void constructor_NullCode_ThrowsException() {
    assertThrows(ClubException.class, () -> new Grupo(null, "Fútbol", 20, 10, 50.0));
  }

  @Test
  @DisplayName("Lanza excepción si el código del grupo está vacío")
  void constructor_EmptyCode_ThrowsException() {
    assertThrows(ClubException.class, () -> new Grupo("", "Fútbol", 20, 10, 50.0));
  }

  @Test
  @DisplayName("Lanza excepción si la actividad del grupo es nula")
  void constructor_NullActivity_ThrowsException() {
    assertThrows(ClubException.class, () -> new Grupo("001", null, 20, 10, 50.0));
  }

  @Test
  @DisplayName("Lanza excepción si la actividad del grupo está vacía")
  void constructor_EmptyActivity_ThrowsException() {
    assertThrows(ClubException.class, () -> new Grupo("001", "", 20, 10, 50.0));
  }

  @Test
  @DisplayName("Lanza excepción si el número de plazas del grupo es menor o igual a 0")
  void constructor_InvalidPlazas_ThrowsException() {
    assertThrows(ClubException.class, () -> new Grupo("001", "Fútbol", 0, 10, 50.0));
  }

  @Test
  @DisplayName("Lanza excepción si el número de matriculados es negativo")
  void constructor_NegativeMatriculados_ThrowsException() {
    assertThrows(ClubException.class, () -> {new Grupo("001", "Fútbol", 20, -1, 50.0);});
  }

  @Test
  @DisplayName("Lanza excepción si el número de matriculados es mayor que las plazas del grupo")
  void constructor_MatriculadosGreaterThanPlazas_ThrowsException() {
    assertThrows(ClubException.class, () -> new Grupo("001", "Fútbol", 20, 25, 50.0));
  }

  @Test
  @DisplayName("Lanza excepción si la tarifa del grupo es menor o igual a 0")
  void constructor_InvalidTarifa_ThrowsException() {
    assertThrows(ClubException.class, () -> new Grupo("001", "Fútbol", 20, 10, 0.0));
  }

  @Test
  @DisplayName("Calcula correctamente las plazas libres del grupo")
  void plazasLibres_CorrectlyCalculated() {
    int expected = grupo.getPlazas() - grupo.getMatriculados();
    int plazasLibres = grupo.plazasLibres();
    assertEquals(expected, plazasLibres);
  }

  @Test
  @DisplayName("Actualiza correctamente el número de plazas del grupo")
  void actualizarPlazas_ValidUpdate_ChangesPlazas() throws ClubException {
    int nuevasPlazas = 25;
    grupo.actualizarPlazas(nuevasPlazas);
    assertEquals(nuevasPlazas, grupo.getPlazas());
  }

  @Test
  @DisplayName("Lanza excepción si se intenta actualizar las plazas a un valor inválido")
  void actualizarPlazas_InvalidValue_ThrowsException() {
    assertThrows(ClubException.class, () -> grupo.actualizarPlazas(0));
  }

  @Test
  @DisplayName("Permite matricular correctamente a nuevos alumnos")
  void matricular_ValidEnrollment_IncreasesMatriculados() throws ClubException {
    int alumnosNuevos = 5;
    int prevMatriculados = grupo.getMatriculados();
    grupo.matricular(alumnosNuevos);
    assertEquals(prevMatriculados + alumnosNuevos, grupo.getMatriculados());
  }

  @Test
  @DisplayName("Lanza excepción si el número de alumnos a matricular es 0")
  void matricular_ZeroStudents_ThrowsException() {
    assertThrows(ClubException.class, () -> grupo.matricular(0));
  }

  @Test
  @DisplayName("Lanza excepción si el número de alumnos a matricular es negativo")
  void matricular_NegativeStudents_ThrowsException() {
    assertThrows(ClubException.class, () -> grupo.matricular(-3));
  }

  @Test
  @DisplayName("Lanza excepción si se intentan matricular más alumnos de los disponibles")
  void matricular_TooManyStudents_ThrowsException() {
    int alumnosMatricular = grupo.plazasLibres() + 1;
    assertThrows(ClubException.class, () -> grupo.matricular(alumnosMatricular));
  }

  @Test
  @DisplayName("Verifica que el método equals funciona correctamente para objetos idénticos")
  void equals_SameObject_ReturnsTrue() {
    Grupo sameGrupo = grupo;
    assertEquals(sameGrupo, grupo);
  }

  @Test
  @DisplayName("Verifica que el método equals detecta correctamente objetos diferentes")
  void equals_DifferentObject_ReturnsFalse() throws ClubException {
    Grupo differentGrupo = new Grupo("002", "Baloncesto", 15, 5, 40.0);
    assertNotEquals(differentGrupo, grupo);
  }

  @Test
  @DisplayName("Verifica que el método equals detecta correctamente cuando el código es diferente")
  void equals_DifferentCode_ReturnsFalse() throws ClubException {
    Grupo differentGrupo = new Grupo("002", "Fútbol", 20, 10, 50.0);
    assertNotEquals(differentGrupo, grupo);
  }

  @Test
  @DisplayName("Verifica que el método equals detecta correctamente cuando la actividad es diferente")
  void equals_DifferentActivity_ReturnsFalse() throws ClubException {
    Grupo differentGrupo = new Grupo("001", "Baloncesto", 20, 10, 50.0);
    assertNotEquals(differentGrupo, grupo);
  }

  @Test
  @DisplayName("Verifica que el método equals devuelve false al comparar con un objeto de otra clase")
  void equals_DifferentClass_ReturnsFalse() {
    String notAGroup = "No soy un grupo";
    assertNotEquals(grupo, notAGroup);
  }

  @Test
  @DisplayName("Verifica que el método hashCode genera valores iguales para objetos equivalentes")
  void hashCode_EqualObjects_ReturnSameHash() throws ClubException {
    Grupo sameGrupo = new Grupo(grupo.getCodigo(), grupo.getActividad(), grupo.getPlazas(), grupo.getMatriculados(), grupo.getTarifa());
    assertEquals(sameGrupo.hashCode(), grupo.hashCode());
  }

  @Test
  @DisplayName("Verifica que el método toString devuelve el formato correcto")
  void toString_ReturnsExpectedFormat() {
    String expected = "(" + grupo.getCodigo() + " - " + grupo.getActividad() + " - " + grupo.getTarifa() + " euros - P:" + grupo.getPlazas() + " - M:" + grupo.getMatriculados() + ")";
    String result = grupo.toString();
    assertEquals(expected, result);
  }
}
