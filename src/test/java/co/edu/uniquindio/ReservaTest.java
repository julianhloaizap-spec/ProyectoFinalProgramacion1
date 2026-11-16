package co.edu.uniquindio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservaTest {
    private static final Logger LOG = Logger.getLogger(ReservaTest.class.getName());
    private Titular titular;
    private Apartamento apartamento;
    private ArrayList<Ocupante> ocupantes;
    private ArrayList<Temporada> temporadas;

    ReservaTest() {
    }

    @BeforeEach
    void ejecutarAntes() {
        LOG.warning("Ejecucion del BeforeEach");
        titular = new Titular("Carlos Pérez", "1094123456", "carlos@mail.com", "3001234567",
                "1234", LocalDate.of(1990, 1, 1), "");
        apartamento = new Apartamento("Suite Café", 1, 3, "Chimenea", 100000);
        ocupantes = new ArrayList<>();
        ocupantes.add(new Ocupante("Carlos Pérez", LocalDate.of(1990, 1, 1)));
        temporadas = new ArrayList<>();
    }

    @AfterEach
    void ejecutarDespues() {
        LOG.warning("Ejecucion del AfterEach");
    }

    // Verifica que la reserva respete estrictamente el ciclo de vida permitido,
    // rechazando saltos inválidos o cambios una vez alcanza un estado final.
    @DisplayName("Prueba transiciones de estado válidas e inválidas de Reserva")
    @Test
    void transicionesDeEstadoPermitidasYBloqueadas() {
        LOG.info("Iniciando prueba transicionesDeEstadoPermitidasYBloqueadas...");
        Reserva r = new Reserva(titular, apartamento, ocupantes,
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), Canal.DIRECTA, "");

        Assertions.assertEquals(EstadoReserva.PENDIENTE, r.getEstadoReserva());
        Assertions.assertFalse(r.cambiarEstado(EstadoReserva.EN_CURSO));
        Assertions.assertFalse(r.cambiarEstado(EstadoReserva.FINALIZADA));

        Assertions.assertTrue(r.cambiarEstado(EstadoReserva.CONFIRMADA));
        Assertions.assertTrue(r.cambiarEstado(EstadoReserva.EN_CURSO));
        Assertions.assertTrue(r.cambiarEstado(EstadoReserva.FINALIZADA));

        Assertions.assertFalse(r.cambiarEstado(EstadoReserva.CANCELADA));
        LOG.info("Finalizando prueba transicionesDeEstadoPermitidasYBloqueadas...");
    }

    // Verifica que al crear la reserva se congelen el valor total, la comisión externa (15%)
    // y la política de cancelación, sin verse afectados por cambios posteriores de tarifas.
    @DisplayName("Prueba congelamiento de tarifas, comisión externa y política de cancelación")
    @Test
    void valorYPoliticaQuedanCongeladosAlCrearReserva() {
        LOG.info("Iniciando prueba valorYPoliticaQuedanCongeladosAlCrearReserva...");
        LocalDate entrada = LocalDate.now().plusDays(10);
        LocalDate salida = LocalDate.now().plusDays(13);

        Reserva r = new Reserva(titular, apartamento, ocupantes, entrada, salida, Canal.EXTERNA, "EXT-999");
        PoliticaCancelacion politicaInicial = new PoliticaCancelacion(7, 2, 50, 100);
        r.liquidarYCongelarValor(temporadas, politicaInicial);

        apartamento.setTarifaTemporada(EstadoTemporada.BAJA, 250000);
        politicaInicial.setPorcentajeRetencionMedia(90);

        Assertions.assertEquals(300000, r.getValorTotalCongelado());
        Assertions.assertEquals(45000, r.getComisionPlataforma());
        Assertions.assertEquals(50, r.getPoliticaCongelada().getPorcentajeRetencionMedia());
        LOG.info("Valor congelado: $" + r.getValorTotalCongelado() + " | Comisión: $" + r.getComisionPlataforma());
        LOG.info("Finalizando prueba valorYPoliticaQuedanCongeladosAlCrearReserva...");
    }

    // Verifica el cálculo de dinero a devolver según la antelación de la cancelación:
    // >7 días devuelve 100%, entre 2 y 7 días retiene 50%, y <48 horas retiene 100%.
    @DisplayName("Prueba cálculo de devolución según política de cancelación")
    @Test
    void calculoDevolucionPorPoliticaDeCancelacion() {
        LOG.info("Iniciando prueba calculoDevolucionPorPoliticaDeCancelacion...");
        LocalDate entrada = LocalDate.now().plusDays(10);
        LocalDate salida = LocalDate.now().plusDays(13);

        Reserva r = new Reserva(titular, apartamento, ocupantes, entrada, salida, Canal.DIRECTA, "");
        r.liquidarYCongelarValor(temporadas, new PoliticaCancelacion());
        r.getFolio().registrarPago(new Pago(MedioPago.TARJETA, 300000));

        Assertions.assertEquals(300000, r.previsualizarDevolucionCancelacion(entrada.minusDays(8)));
        Assertions.assertEquals(150000, r.previsualizarDevolucionCancelacion(entrada.minusDays(5)));
        Assertions.assertEquals(0, r.previsualizarDevolucionCancelacion(entrada.minusDays(1)));
        LOG.info("Finalizando prueba calculoDevolucionPorPoliticaDeCancelacion...");
    }
}

