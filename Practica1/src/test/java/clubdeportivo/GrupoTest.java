package clubdeportivo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GrupoTest {
  private Grupo grupo;

  @BeforeEach
  void setUp() throws ClubException {
    grupo = new Grupo("001", "Fútbol", 20, 10, 50.0);
  }

  @Test
  @DisplayName("Lanza excepción si el código del grupo es nulo")
  void constructor_NullCode_ThrowsException() {
    //Act, Assert
    assertThrows(ClubException.class, () -> new Grupo(null, "Fútbol", 20, 10, 50.0));
  }

  @Test
  @DisplayName("Lanza excepción si el código del grupo está vacío")
  void constructor_EmptyCode_ThrowsException() {
    //Act, Assert
    assertThrows(ClubException.class, () -> new Grupo("", "Fútbol", 20, 10, 50.0));
  }

  @Test
  @DisplayName("Lanza excepción si la actividad del grupo es nula")
  void constructor_NullActivity_ThrowsException() {
    //Act, Assert
    assertThrows(ClubException.class, () -> new Grupo("001", null, 20, 10, 50.0));
  }

  @Test
  @DisplayName("Lanza excepción si la actividad del grupo está vacía")
  void constructor_EmptyActivity_ThrowsException() {
    //Act, Assert
    assertThrows(ClubException.class, () -> new Grupo("001", "", 20, 10, 50.0));
  }

  @Test
  @DisplayName("Lanza excepción si el número de plazas del grupo es menor o igual a 0")
  void constructor_InvalidPlazas_ThrowsException() {
    //Act, Assert
    assertThrows(ClubException.class, () -> new Grupo("001", "Fútbol", 0, 10, 50.0));
  }

  @Test
  @DisplayName("Lanza excepción si el número de matriculados es negativo")
  void constructor_NegativeMatriculados_ThrowsException() {
    //Act, Assert
    assertThrows(ClubException.class, () -> {new Grupo("001", "Fútbol", 20, -1, 50.0);});
  }

  @Test
  @DisplayName("Lanza excepción si el número de matriculados es mayor que las plazas del grupo")
  void constructor_MatriculadosGreaterThanPlazas_ThrowsException() {
    //Act, Assert
    assertThrows(ClubException.class, () -> new Grupo("001", "Fútbol", 20, 25, 50.0));
  }

  @Test
  @DisplayName("Lanza excepción si la tarifa del grupo es menor o igual a 0")
  void constructor_InvalidTarifa_ThrowsException() {
    //Act, Assert
    assertThrows(ClubException.class, () -> new Grupo("001", "Fútbol", 20, 10, 0.0));
  }

  @Test
  @DisplayName("Calcula correctamente las plazas libres del grupo")
  void plazasLibres_CorrectlyCalculated() {
    //Arrange
    int expected = grupo.getPlazas() - grupo.getMatriculados();
    //Act
    int plazasLibres = grupo.plazasLibres();
    //Assert
    assertEquals(expected, plazasLibres);
  }

  @Test
  @DisplayName("Actualiza correctamente el número de plazas del grupo")
  void actualizarPlazas_ValidUpdate_ChangesPlazas() throws ClubException {
    //Arrange
    int nuevasPlazas = 25;
    //Act
    grupo.actualizarPlazas(nuevasPlazas);
    //Assert
    assertEquals(nuevasPlazas, grupo.getPlazas());
  }

  @Test
  @DisplayName("Lanza excepción si se intenta actualizar las plazas a un valor inválido")
  void actualizarPlazas_InvalidValue_ThrowsException() {
    //Act, Assert
    assertThrows(ClubException.class, () -> grupo.actualizarPlazas(0));
  }

  @Test
  @DisplayName("Permite matricular correctamente a nuevos alumnos")
  void matricular_ValidEnrollment_IncreasesMatriculados() throws ClubException {
    //Arrange
    int alumnosNuevos = 5;
    int prevMatriculados = grupo.getMatriculados();
    //Act
    grupo.matricular(alumnosNuevos);
    //Assert
    assertEquals(prevMatriculados + alumnosNuevos, grupo.getMatriculados());
  }

  @Test
  @DisplayName("Lanza excepción si el número de alumnos a matricular es 0")
  void matricular_ZeroStudents_ThrowsException() {
    //Act, Assert
    assertThrows(ClubException.class, () -> grupo.matricular(0));
  }

  @Test
  @DisplayName("Lanza excepción si el número de alumnos a matricular es negativo")
  void matricular_NegativeStudents_ThrowsException() {
    //Act, Assert
    assertThrows(ClubException.class, () -> grupo.matricular(-3));
  }

  @Test
  @DisplayName("Lanza excepción si se intentan matricular más alumnos de los disponibles")
  void matricular_TooManyStudents_ThrowsException() {
    //Arrange
    int alumnosMatricular = grupo.plazasLibres() + 1; //Entendemos que plazasLibres es un getter
    //Act, Assert
    assertThrows(ClubException.class, () -> grupo.matricular(alumnosMatricular));
  }

  @Test
  @DisplayName("Verifica que el método equals funciona correctamente para objetos idénticos")
  void equals_SameObject_ReturnsTrue() {
    //Arrange
    Grupo sameGrupo = grupo;
    //Act, Assert
    assertEquals(sameGrupo, grupo);
  }

  @Test
  @DisplayName("Verifica que el método equals detecta correctamente objetos diferentes")
  void equals_DifferentObject_ReturnsFalse() throws ClubException {
    //Arrange
    Grupo differentGrupo = new Grupo("002", "Baloncesto", 15, 5, 40.0);
    //Act, Assert
    assertNotEquals(differentGrupo, grupo);
  }

  @Test
  @DisplayName("Verifica que el método equals detecta correctamente cuando el código es diferente")
  void equals_DifferentCode_ReturnsFalse() throws ClubException {
    //Arrange
    Grupo differentGrupo = new Grupo("002", "Fútbol", 20, 10, 50.0);
    //Act, Assert
    assertNotEquals(differentGrupo, grupo);
  }

  @Test
  @DisplayName("Verifica que el método equals detecta correctamente cuando la actividad es diferente")
  void equals_DifferentActivity_ReturnsFalse() throws ClubException {
    //Arrange
    Grupo differentGrupo = new Grupo("001", "Baloncesto", 20, 10, 50.0);
    //Act, Assert
    assertNotEquals(differentGrupo, grupo);
  }

  @Test
  @DisplayName("Verifica que el método equals devuelve false al comparar con un objeto de otra clase")
  void equals_DifferentClass_ReturnsFalse() {
    //Arrange
    String notAGroup = "No soy un grupo";
    //Act, Assert
    assertNotEquals(grupo, notAGroup);
  }

  @Test
  @DisplayName("Verifica que el método hashCode genera valores iguales para objetos equivalentes")
  void hashCode_EqualObjects_ReturnSameHash() throws ClubException {
    //Arrange
    Grupo sameGrupo = new Grupo(grupo.getCodigo(), grupo.getActividad(), grupo.getPlazas(), grupo.getMatriculados(), grupo.getTarifa());
    //Act, Assert
    assertEquals(sameGrupo.hashCode(), grupo.hashCode());
  }

  @Test
  @DisplayName("Verifica que el método toString devuelve el formato correcto")
  void toString_ReturnsExpectedFormat() {
    //Arrange
    String expected = "(" + grupo.getCodigo() + " - " + grupo.getActividad() + " - " + grupo.getTarifa() + " euros - P:" + grupo.getPlazas() + " - M:" + grupo.getMatriculados() + ")";
    //Act
    String result = grupo.toString();
    //Assert
    assertEquals(expected, result);
  }
}
