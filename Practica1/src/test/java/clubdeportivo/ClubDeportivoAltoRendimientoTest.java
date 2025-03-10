package clubdeportivo;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ClubDeportivoAltoRendimientoTest {
    

    @Test
    @DisplayName("Lanza excepción si el máximo de personas es 0")
    void constructor_ZeroMaxPeople_ThrowsException() {
        // Act & Assert
        assertThrows(ClubException.class, () -> new ClubDeportivoAltoRendimiento("Club Elite", 0, 10));
    }
}
