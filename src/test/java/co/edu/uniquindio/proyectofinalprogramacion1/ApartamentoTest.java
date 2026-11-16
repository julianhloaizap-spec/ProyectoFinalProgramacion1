package co.edu.uniquindio.proyectofinalprogramacion1;

import java.time.LocalDate;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ApartamentoTest {
    private static final Logger LOG = Logger.getLogger(ApartamentoTest.class.getName());
    private Apartamento apartamento;

    ApartamentoTest() {
    }

    @BeforeEach
    void ejecutarAntes() {
        LOG.warning("Ejecucion del BeforeEach");
        apartamento = new Apartamento("Suite Cordillera", 2, 4, "Balcón, Vista a montaña", 100000);
    }

    @AfterEach
    void ejecutarDespues() {
        LOG.warning("Ejecucion del AfterEach");
    }

    // Verifica que al instanciar un apartamento se genere su ID automático (APT-XXX),
    // cumpla las validaciones básicas e inicie en estado operativo PREPARADO.
    @DisplayName("Prueba generación de ID y validación de Apartamento")
    @Test
    void generarIdAutomaticoYValidarDatos() {
        LOG.info("Iniciando prueba generarIdAutomaticoYValidarDatos...");
        Assertions.assertNotNull(apartamento.getIdApartamento());
        Assertions.assertTrue(apartamento.getIdApartamento().startsWith("APT-"));
        Assertions.assertTrue(apartamento.esValido());
        Assertions.assertEquals(EstadoOperativo.PREPARADO, apartamento.getEstadoOperativo());
        LOG.info("ID generado: " + apartamento.getIdApartamento());
        LOG.info("Finalizando prueba generarIdAutomaticoYValidarDatos...");
    }

    // Verifica el cálculo de tarifas por defecto según temporada (Baja, Media, Alta)
    // y que se pueda modificar manualmente la tarifa de una temporada específica.
    @DisplayName("Prueba tarifas por temporada por defecto y personalizadas")
    @Test
    void tarifasPorTemporadaPorDefectoYPersonalizadas() {
        LOG.info("Iniciando prueba tarifasPorTemporadaPorDefectoYPersonalizadas...");
        Assertions.assertEquals(100000, apartamento.getTarifaPorTemporada(EstadoTemporada.BAJA));
        Assertions.assertEquals(130000, apartamento.getTarifaPorTemporada(EstadoTemporada.MEDIA));
        Assertions.assertEquals(150000, apartamento.getTarifaPorTemporada(EstadoTemporada.ALTA));

        apartamento.setTarifaTemporada(EstadoTemporada.ALTA, 180000);
        Assertions.assertEquals(180000, apartamento.getTarifaPorTemporada(EstadoTemporada.ALTA));
        LOG.info("Finalizando prueba tarifasPorTemporadaPorDefectoYPersonalizadas...");
    }

    // Verifica que no se permita pasar un apartamento a FUERA_DE_SERVICIO sin motivo
    // y que dicho motivo se limpie automáticamente cuando vuelve a estar disponible.
    @DisplayName("Prueba cambio de estado operativo Fuera de Servicio con motivo obligatorio")
    @Test
    void cambioEstadoFueraDeServicioRequiereMotivo() {
        LOG.info("Iniciando prueba cambioEstadoFueraDeServicioRequiereMotivo...");
        Assertions.assertFalse(apartamento.cambiarEstadoOperativo(EstadoOperativo.FUERA_DE_SERVICIO, ""));
        Assertions.assertFalse(apartamento.cambiarEstadoOperativo(EstadoOperativo.FUERA_DE_SERVICIO, null));
        Assertions.assertEquals(EstadoOperativo.PREPARADO, apartamento.getEstadoOperativo());

        Assertions.assertTrue(apartamento.cambiarEstadoOperativo(EstadoOperativo.FUERA_DE_SERVICIO, "Daño en tubería"));
        Assertions.assertEquals(EstadoOperativo.FUERA_DE_SERVICIO, apartamento.getEstadoOperativo());
        Assertions.assertEquals("Daño en tubería", apartamento.getMotivoFueraServicio());

        Assertions.assertTrue(apartamento.cambiarEstadoOperativo(EstadoOperativo.PREPARADO, null));
        Assertions.assertEquals("", apartamento.getMotivoFueraServicio());
        LOG.info("Finalizando prueba cambioEstadoFueraDeServicioRequiereMotivo...");
    }

    // Verifica que el apartamento detecte si un rango de fechas se cruza con un bloqueo
    // y permita reservar exactamente desde el día en que finaliza dicho bloqueo.
    @DisplayName("Prueba bloqueo de fechas y control de solapamiento")
    @Test
    void bloqueoDeFechasYSolapamiento() {
        LOG.info("Iniciando prueba bloqueoDeFechasYSolapamiento...");
        LocalDate inicioBloqueo = LocalDate.now().plusDays(10);
        LocalDate finBloqueo = LocalDate.now().plusDays(15);
        Bloqueo bloqueo = new Bloqueo(inicioBloqueo, finBloqueo, "Mantenimiento eléctrico");

        apartamento.agregarBloqueo(bloqueo);

        Assertions.assertTrue(apartamento.estaBloqueado(LocalDate.now().plusDays(12), LocalDate.now().plusDays(14)));
        Assertions.assertFalse(apartamento.estaBloqueado(LocalDate.now().plusDays(15), LocalDate.now().plusDays(18)));
        LOG.info("Finalizando prueba bloqueoDeFechasYSolapamiento...");
    }
}