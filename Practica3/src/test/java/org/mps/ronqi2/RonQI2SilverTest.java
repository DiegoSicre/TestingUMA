package org.mps.ronqi2;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mps.dispositivo.Dispositivo;
import org.mps.dispositivo.DispositivoSilver;

@ExtendWith(MockitoExtension.class)
public class RonQI2SilverTest {

    @Mock
    private Dispositivo dispositivoMock;

    @InjectMocks
    private RonQI2Silver ronQI2Silver;

    @BeforeEach
    void setUp() {
        ronQI2Silver = new RonQI2Silver();
        ronQI2Silver.anyadirDispositivo(dispositivoMock);
    }

    @Nested
    @DisplayName("Tests de inicializar")
    class InicializarTests {

        @BeforeEach
        public void init() {
            // Inicializar el mock del dispositivo y la instancia de RonQI2Silver
            dispositivoMock = mock(DispositivoSilver.class);
            ronQI2Silver = new RonQI2Silver();
            ronQI2Silver.anyadirDispositivo(dispositivoMock);
        }

        @Test
        @DisplayName("Inicializar conecta y configura ambos sensores correctamente")
        void inicializar_correctlyConnectAndConfigureSensors_returnsTrue() {
            // Arrange
            when(dispositivoMock.conectarSensorPresion()).thenReturn(true);
            when(dispositivoMock.configurarSensorPresion()).thenReturn(true);
            when(dispositivoMock.conectarSensorSonido()).thenReturn(true);
            when(dispositivoMock.configurarSensorSonido()).thenReturn(true);

            // Act
            boolean result = ronQI2Silver.inicializar();

            // Assert
            assertTrue(result);
            verify(dispositivoMock, times(1)).conectarSensorPresion();
            verify(dispositivoMock, times(1)).configurarSensorPresion();
            verify(dispositivoMock, times(1)).conectarSensorSonido();
            verify(dispositivoMock, times(1)).configurarSensorSonido();
        }

        @Test
        @DisplayName("Inicializar falla al conectar sensor de presión")
        void inicializar_failToConnectPressureSensor_returnsFalse() {
            // Arrange
            when(dispositivoMock.conectarSensorPresion()).thenReturn(false);

            // Act
            boolean result = ronQI2Silver.inicializar();

            // Assert
            assertFalse(result);
            verify(dispositivoMock, times(1)).conectarSensorPresion();
            verify(dispositivoMock, never()).configurarSensorPresion();
            verify(dispositivoMock, never()).conectarSensorSonido();
            verify(dispositivoMock, never()).configurarSensorSonido();
        }

        @Test
        @DisplayName("Inicializar falla al conectar sensor de sonido")
        void inicializar_failToConnectSoundSensor_returnsFalse() {
            // Arrange
            when(dispositivoMock.conectarSensorPresion()).thenReturn(true);
            when(dispositivoMock.configurarSensorPresion()).thenReturn(true);
            when(dispositivoMock.conectarSensorSonido()).thenReturn(false);

            // Act
            boolean result = ronQI2Silver.inicializar();

            // Assert
            assertFalse(result);
            verify(dispositivoMock, times(1)).conectarSensorPresion();
            verify(dispositivoMock, times(1)).configurarSensorPresion();
            verify(dispositivoMock, times(1)).conectarSensorSonido();
            verify(dispositivoMock, never()).configurarSensorSonido();
        }

        @Test
        @DisplayName("Inicializar conecta pero no configura el sensor de presión")
        void inicializar_connectButNoConfigurePressureSensor_returnsFalse() {
            // Arrange
            when(dispositivoMock.conectarSensorPresion()).thenReturn(true);
            when(dispositivoMock.configurarSensorPresion()).thenReturn(false);
            when(dispositivoMock.conectarSensorSonido()).thenReturn(true);
            when(dispositivoMock.configurarSensorSonido()).thenReturn(true);

            // Act
            boolean result = ronQI2Silver.inicializar();

            // Assert
            assertFalse(result);
            verify(dispositivoMock, times(1)).conectarSensorPresion();
            verify(dispositivoMock, times(1)).configurarSensorPresion();
            verify(dispositivoMock, times(1)).conectarSensorSonido();
            verify(dispositivoMock, times(1)).configurarSensorSonido();
        }

        @Test
        @DisplayName("Inicializar conecta pero no configura el sensor de sonido")
        void inicializar_connectButNoConfigureSoundSensor_returnsFalse() {
            // Arrange
            when(dispositivoMock.conectarSensorPresion()).thenReturn(true);
            when(dispositivoMock.configurarSensorPresion()).thenReturn(true);
            when(dispositivoMock.conectarSensorSonido()).thenReturn(true);
            when(dispositivoMock.configurarSensorSonido()).thenReturn(false);

            // Act
            boolean result = ronQI2Silver.inicializar();

            // Assert
            assertFalse(result);
            verify(dispositivoMock, times(1)).conectarSensorPresion();
            verify(dispositivoMock, times(1)).configurarSensorPresion();
            verify(dispositivoMock, times(1)).conectarSensorSonido();
            verify(dispositivoMock, times(1)).configurarSensorSonido();
        }
    }


    @Nested
    @DisplayName("Tests de reconectar")
    class ReconectarTests {

        @BeforeEach
        public void init() {
            // Inicializar el mock del dispositivo y la instancia de RonQI2Silver
            dispositivoMock = mock(DispositivoSilver.class);
            ronQI2Silver = new RonQI2Silver();
            ronQI2Silver.anyadirDispositivo(dispositivoMock);
        }

        @Test
        @DisplayName("Reconectar cuando está desconectado y reconecta bien")
        void reconectar_disconnectedDevice_reconnectsSuccessfully_returnsTrue() {
            // Arrange
            when(dispositivoMock.estaConectado()).thenReturn(false);
            when(dispositivoMock.conectarSensorPresion()).thenReturn(true);
            when(dispositivoMock.conectarSensorSonido()).thenReturn(true);

            // Act
            boolean result = ronQI2Silver.reconectar();

            // Assert
            assertTrue(result);
            verify(dispositivoMock, times(1)).estaConectado();
            verify(dispositivoMock, times(1)).conectarSensorPresion();
            verify(dispositivoMock, times(1)).conectarSensorSonido();
        }

        @Test
        @DisplayName("Reconectar cuando ya está conectado devuelve false")
        void reconectar_deviceAlreadyConnected_returnsFalse() {
            // Arrange
            when(dispositivoMock.estaConectado()).thenReturn(true);

            // Act
            boolean result = ronQI2Silver.reconectar();

            // Assert
            assertFalse(result);
            verify(dispositivoMock, times(1)).estaConectado();
            verify(dispositivoMock, never()).conectarSensorPresion();
            verify(dispositivoMock, never()).conectarSensorSonido();
        }

        @Test
        @DisplayName("Reconectar cuando el dispositivo está desconectado y falla la reconexión del sensor de presión")
        void reconectar_disconnectedDevice_failToReconnectPressureSensor_returnsFalse() {
            // Arrange
            when(dispositivoMock.estaConectado()).thenReturn(false);
            when(dispositivoMock.conectarSensorPresion()).thenReturn(false); // Falla la reconexión del sensor de presión

            // Act
            boolean result = ronQI2Silver.reconectar();

            // Assert
            assertFalse(result);
            verify(dispositivoMock, times(1)).estaConectado();
            verify(dispositivoMock, times(1)).conectarSensorPresion();
            verify(dispositivoMock, never()).conectarSensorSonido();
        }

        @Test
        @DisplayName("Reconectar cuando el dispositivo está desconectado y falla la reconexión del sensor de sonido")
        void reconectar_disconnectedDevice_failToReconnectSoundSensor_returnsFalse() {
            // Arrange
            when(dispositivoMock.estaConectado()).thenReturn(false);
            when(dispositivoMock.conectarSensorPresion()).thenReturn(true);
            when(dispositivoMock.conectarSensorSonido()).thenReturn(false); // Falla la reconexión del sensor de sonido

            // Act
            boolean result = ronQI2Silver.reconectar();

            // Assert
            assertFalse(result);
            verify(dispositivoMock, times(1)).estaConectado();
            verify(dispositivoMock, times(1)).conectarSensorPresion();
            verify(dispositivoMock, times(1)).conectarSensorSonido();
        }

        @Test
        @DisplayName("Reconectar cuando el dispositivo está desconectado y fallan ambos sensores al reconectarse")
        void reconectar_disconnectedDevice_failToReconnectBothSensors_returnsFalse() {
            // Arrange
            when(dispositivoMock.estaConectado()).thenReturn(false);
            when(dispositivoMock.conectarSensorPresion()).thenReturn(false); // Falla la reconexión del sensor de presión
          
            // Act
            boolean result = ronQI2Silver.reconectar();

            // Assert
            assertFalse(result);
            verify(dispositivoMock, times(1)).estaConectado();
            verify(dispositivoMock, times(1)).conectarSensorPresion();
            verify(dispositivoMock,never()).conectarSensorSonido();
        }
    }


     
 /* Realiza un primer test para ver que funciona bien independientemente del número de lecturas.
     */

    @Nested
    @DisplayName("Tests de obtenerNuevaLectura")
    class ObtenerNuevaLecturaTests {

        @Test
        @DisplayName("Obtener nueva lectura añade lecturas y mantiene el tamaño")
        void obtenerNuevaLectura_addsNewReadingAndMaintainsListSize() {
            // Arrange
            when(dispositivoMock.leerSensorPresion()).thenReturn(22.0f);
            when(dispositivoMock.leerSensorSonido()).thenReturn(32.0f);

            // Act
            IntStream.range(0, 7).forEach(i -> ronQI2Silver.obtenerNuevaLectura());
            boolean hayApnea = ronQI2Silver.evaluarApneaSuenyo();

            // Assert
            assertTrue(hayApnea);
        }
    }
    /*
     * El método evaluarApneaSuenyo, evalua las últimas 5 lecturas realizadas con obtenerNuevaLectura(), 
     * y si ambos sensores superan o son iguales a sus umbrales, que son thresholdP = 20.0f y thresholdS = 30.0f;, 
     * se considera que hay una apnea en proceso. Si hay menos de 5 lecturas también debería realizar la media.
     * Usa el ParameterizedTest para realizar un número de lecturas previas a calcular si hay apnea o no (por ejemplo 4, 5 y 10 lecturas).
     * https://junit.org/junit5/docs/current/user-guide/index.html#writing-tests-parameterized-tests
     */
    @Nested
    @DisplayName("Tests de evaluarApneaSuenyo")
    class EvaluarApneaSuenyoTests {

        @ParameterizedTest(name = "Con {0} lecturas")
        @ValueSource(ints = {4, 5, 10})
        @DisplayName("Detecta correctamente apnea independientemente del número de lecturas")
        void evaluarApneaSuenyo_detectsApneaCorrectly_withVariousReadings(int numLecturas) {
            // Arrange
            when(dispositivoMock.leerSensorPresion()).thenReturn(20.0f); //Valores umbral en ambos casos 
            when(dispositivoMock.leerSensorSonido()).thenReturn(30.0f);
            IntStream.range(0, numLecturas).forEach(i -> ronQI2Silver.obtenerNuevaLectura());

            // Act
            boolean result = ronQI2Silver.evaluarApneaSuenyo();

            // Assert
            assertTrue(result);
        }

        @Test
        @DisplayName("No detecta apnea cuando las lecturas son bajas")
        void evaluarApneaSuenyo_doesNotDetectApnea_withLowReadings() {
            // Arrange
            when(dispositivoMock.leerSensorPresion()).thenReturn(19.9f); //Límite umbral en ambos casos
            when(dispositivoMock.leerSensorSonido()).thenReturn(29.9f);
            IntStream.range(0, 5).forEach(i -> ronQI2Silver.obtenerNuevaLectura());

            // Act
            boolean result = ronQI2Silver.evaluarApneaSuenyo();

            // Assert
            assertFalse(result);
        }

        @Test
        @DisplayName("No detecta apnea cuando solo la presión supera el umbral")
        void evaluarApneaSuenyo_doesNotDetectApnea_whenOnlyPressureIsAboveThreshold() {
            // Arrange
            when(dispositivoMock.leerSensorPresion()).thenReturn(25.0f);  // >20
            when(dispositivoMock.leerSensorSonido()).thenReturn(15.0f);  // <30
            IntStream.range(0, 5).forEach(i -> ronQI2Silver.obtenerNuevaLectura());

            // Act
            boolean result = ronQI2Silver.evaluarApneaSuenyo();

            // Assert
            assertFalse(result); // Debería ser false, ya que el sonido está por debajo del umbral.
        }

        @Test
        @DisplayName("No detecta apnea cuando solo el sonido supera el umbral")
        void evaluarApneaSuenyo_doesNotDetectApnea_whenOnlySoundIsAboveThreshold() {
            // Arrange
            when(dispositivoMock.leerSensorPresion()).thenReturn(19.0f);  // <20
            when(dispositivoMock.leerSensorSonido()).thenReturn(35.0f);  // >30
            IntStream.range(0, 5).forEach(i -> ronQI2Silver.obtenerNuevaLectura());

            // Act
            boolean result = ronQI2Silver.evaluarApneaSuenyo();

            // Assert
            assertFalse(result); // Debería ser false, ya que la presión está por debajo del umbral.
        }
    }
}
