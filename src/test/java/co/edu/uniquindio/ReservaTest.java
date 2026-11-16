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

