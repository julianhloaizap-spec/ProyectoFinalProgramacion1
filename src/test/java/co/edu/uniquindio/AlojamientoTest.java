package co.edu.uniquindio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AlojamientoTest {
    private static final Logger LOG = Logger.getLogger(AlojamientoTest.class.getName());
    private Alojamiento alojamiento;
    private Titular titular;
    private Apartamento apartamento;
    private ArrayList<Ocupante> ocupantes;

    AlojamientoTest() {
    }

    @BeforeEach
    void ejecutarAntes() {
        LOG.warning("Ejecucion del BeforeEach");
        alojamiento = new Alojamiento("900123456-1", "Aparta-Suites UQ", "Salento");
        titular = new Titular("Laura Díaz", "1094999888", "laura@mail.com", "3120001122",
                "pass", LocalDate.of(1995, 3, 20), "");
        apartamento = new Apartamento("Suite Mirador", 1, 2, "Balcón", 100000);

        alojamiento.registrarUsuario(titular);
        alojamiento.registrarApartamento(apartamento);

        ocupantes = new ArrayList<>();
        ocupantes.add(new Ocupante("Laura Díaz", LocalDate.of(1995, 3, 20)));
    }

    @AfterEach
    void ejecutarDespues() {
        LOG.warning("Ejecucion del AfterEach");
    }

    // Verifica que dos reservas activas no compartan noches,
    // pero permitiendo que una nueva reserva entre el mismo día en que sale la anterior.
    @DisplayName("Prueba control anti-doble venta y entrada el mismo día de salida")
    @Test
    void evitarDobleVentaYPermitirReservaElDiaDeSalida() {
        LOG.info("Iniciando prueba evitarDobleVentaYPermitirReservaElDiaDeSalida...");
        LocalDate entrada1 = LocalDate.now().plusDays(2);
        LocalDate salida1 = LocalDate.now().plusDays(5);

        Reserva r1 = alojamiento.crearReserva(titular.getIdUsuario(), apartamento.getIdApartamento(),
                ocupantes, entrada1, salida1, Canal.DIRECTA, "");
        Assertions.assertNotNull(r1);

        Assertions.assertThrows(IllegalStateException.class, () ->
                alojamiento.crearReserva(titular.getIdUsuario(), apartamento.getIdApartamento(),
                        ocupantes, LocalDate.now().plusDays(4), LocalDate.now().plusDays(6), Canal.PORTAL, "")
        );

        Reserva r2 = alojamiento.crearReserva(titular.getIdUsuario(), apartamento.getIdApartamento(),
                ocupantes, salida1, LocalDate.now().plusDays(7), Canal.PORTAL, "");
        Assertions.assertNotNull(r2);
        LOG.info("Finalizando prueba evitarDobleVentaYPermitirReservaElDiaDeSalida...");
    }

    // Verifica que todos los ocupantes (incluidos los menores de 6 años que no pagan)
    // cuenten para el límite de capacidad máxima del apartamento.
    @DisplayName("Prueba validación de capacidad máxima incluyendo menores")
    @Test
    void validarCapacidadMaximaIncluyendoMenores() {
        LOG.info("Iniciando prueba validarCapacidadMaximaIncluyendoMenores...");
        ocupantes.add(new Ocupante("Adulto 2", LocalDate.of(1994, 1, 1)));
        ocupantes.add(new Ocupante("Bebé", LocalDate.now().minusYears(1)));

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                alojamiento.crearReserva(titular.getIdUsuario(), apartamento.getIdApartamento(),
                        ocupantes, LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), Canal.PORTAL, "")
        );
        LOG.info("Finalizando prueba validarCapacidadMaximaIncluyendoMenores...");
    }

    // Verifica que si una reserva del canal Externo choca con fechas ya ocupadas,
    // se rechace sin reemplazar la existente y quede registrada como conflicto de canal.
    @DisplayName("Prueba rechazo y registro de conflicto en canal externo")
    @Test
    void rechazoYRegistroConflictoDeCanalExterno() {
        LOG.info("Iniciando prueba rechazoYRegistroConflictoDeCanalExterno...");
        LocalDate entrada = LocalDate.now().plusDays(3);
        LocalDate salida = LocalDate.now().plusDays(6);

        alojamiento.crearReserva(titular.getIdUsuario(), apartamento.getIdApartamento(),
                ocupantes, entrada, salida, Canal.DIRECTA, "");

        Assertions.assertThrows(IllegalStateException.class, () ->
                alojamiento.crearReserva(titular.getIdUsuario(), apartamento.getIdApartamento(),
                        ocupantes, entrada, salida, Canal.EXTERNA, "BOOKING-777")
        );

        Assertions.assertEquals(1, alojamiento.getListConflictosCanal().size());
        Assertions.assertEquals("BOOKING-777", alojamiento.getListConflictosCanal().get(0).getCodigoExterno());
        LOG.info("Finalizando prueba rechazoYRegistroConflictoDeCanalExterno...");
    }

    // Verifica los horarios operativos de check-in (desde las 3:00 p. m.), el cambio de estado
    // al hacer check-out y que transcurran mínimo 3 horas de preparación entre grupos.
    @DisplayName("Prueba flujo de Check-In, Check-Out y 3 horas de preparación")
    @Test
    void flujoCheckInCheckOutYTiempoDePreparacion() {
        LOG.info("Iniciando prueba flujoCheckInCheckOutYTiempoDePreparacion...");
        LocalDate entrada1 = LocalDate.now().plusDays(1);
        LocalDate salida1 = LocalDate.now().plusDays(2);

        Reserva r1 = alojamiento.crearReserva(titular.getIdUsuario(), apartamento.getIdApartamento(),
                ocupantes, entrada1, salida1, Canal.DIRECTA, "");
        alojamiento.confirmarReserva(r1.getIdReserva());

        Assertions.assertFalse(alojamiento.realizarCheckIn(r1.getIdReserva(), entrada1.atTime(14, 0)));

        Assertions.assertTrue(alojamiento.realizarCheckIn(r1.getIdReserva(), entrada1.atTime(15, 0)));
        Assertions.assertEquals(EstadoOperativo.OCUPADO, apartamento.getEstadoOperativo());

        LocalDateTime horaSalida = salida1.atTime(13, 0);
        Assertions.assertTrue(alojamiento.realizarCheckOut(r1.getIdReserva(), horaSalida));
        Assertions.assertEquals(EstadoOperativo.PENDIENTE_PREPARACION, apartamento.getEstadoOperativo());

        Reserva r2 = alojamiento.crearReserva(titular.getIdUsuario(), apartamento.getIdApartamento(),
                ocupantes, salida1, salida1.plusDays(2), Canal.DIRECTA, "");
        alojamiento.confirmarReserva(r2.getIdReserva());
        apartamento.cambiarEstadoOperativo(EstadoOperativo.PREPARADO, null);

        Assertions.assertFalse(alojamiento.realizarCheckIn(r2.getIdReserva(), salida1.atTime(15, 0)));
        Assertions.assertTrue(alojamiento.realizarCheckIn(r2.getIdReserva(), salida1.atTime(16, 0)));
        LOG.info("Finalizando prueba flujoCheckInCheckOutYTiempoDePreparacion...");
    }

    // Verifica que una reserva confirmada solo pueda declararse como NO_SHOW
    // después de las 10:00 p. m. del día programado para la entrada.
    @DisplayName("Prueba declaración de No-Show después de las 10:00 p. m.")
    @Test
    void declaracionNoShowDespuesDeLasDiezPM() {
        LOG.info("Iniciando prueba declaracionNoShowDespuesDeLasDiezPM...");
        LocalDate entrada = LocalDate.now().plusDays(1);
        Reserva r = alojamiento.crearReserva(titular.getIdUsuario(), apartamento.getIdApartamento(),
                ocupantes, entrada, entrada.plusDays(2), Canal.DIRECTA, "");
        alojamiento.confirmarReserva(r.getIdReserva());

        Assertions.assertFalse(alojamiento.declararNoShow(r.getIdReserva(), entrada.atTime(21, 59)));
        Assertions.assertTrue(alojamiento.declararNoShow(r.getIdReserva(), entrada.atTime(22, 1)));
        Assertions.assertEquals(EstadoReserva.NO_SHOW, r.getEstadoReserva());
        LOG.info("Finalizando prueba declaracionNoShowDespuesDeLasDiezPM...");
    }
}