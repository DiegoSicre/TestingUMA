import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import es.uma.informatica.mps.Calendario;

/**
 * Tests diseñados con particionamiento de equivalencia.
 * Cada test inválido cubre una sola CE (Regla 3).
 * Los tests válidos pueden cubrir múltiples CE válidas (Regla 2).
 */
public class CalendarioTest {

    /* -------------------------------------------------
     * CLASES DE EQUIVALENCIA INVÁLIDAS  (esperan excepción)
     * ------------------------------------------------- */

    @Test @DisplayName("Mes < 1  ⇒ IllegalArgumentException")
    @Tag("1")
    public void diaSemana_MonthLessThanOne_ExceptionThrown() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                     () -> Calendario.diaSemana(10, 0, 2025));
    }

    @Test @DisplayName("Mes > 12 ⇒ IllegalArgumentException")
    @Tag("3")
    public void diaSemana_MonthGreaterThanTwelve_ExceptionThrown() {
        assertThrows(IllegalArgumentException.class,
                     () -> Calendario.diaSemana(3, 13, 2025));   // Arrange+Act
    }

    @Test @DisplayName("Día < 1 ⇒ IllegalArgumentException")
    @Tag("4")
    public void diaSemana_DayLessThanOne_ExceptionThrown() {
        assertThrows(IllegalArgumentException.class,
                     () -> Calendario.diaSemana(0, 1, 2025));
    }

    @Test @DisplayName("Día > 31 en mes de 31 días ⇒ IllegalArgumentException")
    @Tag("6")
    public void diaSemana_DayGreaterThan31_ExceptionThrown() {
        assertThrows(IllegalArgumentException.class,
                     () -> Calendario.diaSemana(32, 1, 2025));
    }

    @Test @DisplayName("31‑abr (mes de 30 días) ⇒ IllegalArgumentException")
    @Tag("8")
    public void diaSemana_ThirtyOneInApril_ExceptionThrown() {
        assertThrows(IllegalArgumentException.class,
                     () -> Calendario.diaSemana(31, 4, 2025));
    }

    @Test @DisplayName("29‑feb año NO bisiesto ⇒ IllegalArgumentException")
    @Tag("11")
    public void diaSemana_Feb29InNonLeapYear_ExceptionThrown() {
        assertThrows(IllegalArgumentException.class,
                     () -> Calendario.diaSemana(29, 2, 2023));
    }

    @Test @DisplayName("30‑feb (fecha inexistente) en año bisiesto ⇒ IllegalArgumentException")
    @Tag("12")
    public void diaSemana_FebruaryThirty_ExceptionThrown() {
        assertThrows(IllegalArgumentException.class,
                     () -> Calendario.diaSemana(30, 2, 2024));
    }

    @Test @DisplayName("Fecha < 01‑mar‑0004 ⇒ IllegalArgumentException")
    @Tag("13")
    public void diaSemana_BeforeJulianStart_ExceptionThrown() {
        assertThrows(IllegalArgumentException.class,
                     () -> Calendario.diaSemana(28, 2, 4));
    }

    @Test @DisplayName("Hueco 5‑14 oct 1582 ⇒ IllegalArgumentException")
    @Tag("15")
    public void diaSemana_DateInGregorianGap_ExceptionThrown() {
        assertThrows(IllegalArgumentException.class,
                     () -> Calendario.diaSemana(10, 10, 1582));
    }

    @Test @DisplayName("29‑feb‑1900 (mutiplo 100 pero no divisible ×4) ⇒ IllegalArgumentException")
    @Tag("20")
    public void diaSemana_Feb29InCenturyNotLeap_ExceptionThrown() {
        assertThrows(IllegalArgumentException.class,
                     () -> Calendario.diaSemana(29, 2, 1800));
    }

    @Test
    @DisplayName("29‑feb‑0005 (año juliano no divisible ×4) ⇒ IllegalArgumentException")
    @Tag("21") @Tag("C--5")
    public void diaSemana_Feb29InJulianNonLeapYear_ExceptionThrown() {
        assertThrows(IllegalArgumentException.class,
                    () -> Calendario.diaSemana(29, 2, 5));
    }

    //Se sigue MCD
   /* -------------------------------------------------
     * CLASES DE EQUIVALENCIA VÁLIDAS (esperan ejecución sin excepción)
     * Se agrupan múltiples CE válidas en cada test si corresponde
     * ------------------------------------------------- */

    // mes válido | fecha ≥ 01‑mar‑0004
    @Test @DisplayName("Fecha con mes válido (17‑abr‑2025)")
    @Tag("2") @Tag("14") @Tag("7") @Tag("16")
    public void diaSemana_ValidAprilDate_DoesNotThrow() {
        assertDoesNotThrow(() -> Calendario.diaSemana(17, 4, 2025));
    }
    // día válido en mes de 31 días 
    @Test @DisplayName("31‑ene‑2025 (mes de 31 días)")
    @Tag("5") @Tag("2") @Tag("14") @Tag("16")
    public void diaSemana_JanuaryThirtyFirst_DoesNotThrow() {
        assertDoesNotThrow(() -> Calendario.diaSemana(31, 1, 2025));
    }
    // CE‑7: día válido en mes de 30 días | CE‑2, CE‑14
    /*@Test @DisplayName("CE‑7 – 30‑abr‑2025 (mes de 30 días) → WEDNESDAY")
    @Tag("7") @Tag("2") @Tag("14")
    public void diaSemana_AprilThirty_DoesNotThrow() {
        assertDoesNotThrow(() -> Calendario.diaSemana(30, 4, 2025));
    } Sobra por cubrise en CE-2*/
    
    // febrero 28 en no bisiesto 
    @Test @DisplayName("28‑feb‑2023 (no bisiesto) → TUESDAY")
    @Tag("9") @Tag("2") @Tag("14")@Tag("16")
    public void diaSemana_FebruaryTwentyEightNonLeap_DoesNotThrow() {
        assertDoesNotThrow(() -> Calendario.diaSemana(28, 2, 2023));
    }
    // 29‑feb‑2024 (bisiesto por divisible ×4 y ¬×100)
    @Test @DisplayName("29‑feb‑2024 (bisiesto) → THURSDAY")
    @Tag("10") @Tag("19") @Tag("2") @Tag("14")@Tag("16")
    public void diaSemana_Feb29LeapYear_DoesNotThrow() {
        assertDoesNotThrow(() -> Calendario.diaSemana(29, 2, 2024));
    }
    // frontera de fechas válidas (Ya probado -> Caso límite)
    @Test @DisplayName("01‑mar‑0004 (fecha mínima válida) → MONDAY")
    @Tag("14") @Tag("2") 
    public void diaSemana_FirstValidJulianDate_DoesNotThrow() {
        assertDoesNotThrow(() -> Calendario.diaSemana(1, 3, 4));
    }

    // primer día válido después del hueco gregoriano (Ya probado -> caso límite)
    @Test @DisplayName("15‑oct‑1582 (primer día Gregoriano) → FRIDAY")
    @Tag("16") @Tag("14") @Tag("2")
    public void diaSemana_FirstGregorianDate_DoesNotThrow() {
        assertDoesNotThrow(() -> Calendario.diaSemana(15, 10, 1582));
    }
    // año juliano divisible ×4 → bisiesto
    @Test @DisplayName("29‑feb‑0008 (bisiesto Juliano) → FRIDAY")
    @Tag("17") @Tag("14") @Tag("2") @Tag("22") @Tag("10")
    public void diaSemana_LeapYearJulian_DoesNotThrow() {
        assertDoesNotThrow(() -> Calendario.diaSemana(29, 2, 8));
    }
     // año gregoriano divisible ×400 → bisiesto
    @Test @DisplayName("29‑feb‑2000 (divisible ×400) → TUESDAY")
    @Tag("18") @Tag("2") @Tag("14")@Tag("10") @Tag("16")
    public void diaSemana_LeapYearDivisibleBy400_DoesNotThrow() {
        assertDoesNotThrow(() -> Calendario.diaSemana(29, 2, 2000));
    }
    /* Ya comprobado@Test @DisplayName("CE‑23 – Fecha juliana válida (10‑oct‑1500)")
    @Tag("22") @Tag("14")
    public void diaSemana_ValidJulianDateBeforeGregorian_DoesNotThrow() {
        assertDoesNotThrow(() -> Calendario.diaSemana(10, 10, 1500));
    }*/

}
