package co.edu.uniquindio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FolioTest {
    private static final Logger LOG = Logger.getLogger(FolioTest.class.getName());
    private Folio folio;
    private Apartamento apartamento;
    private ArrayList<Temporada> temporadas;

    FolioTest() {
    }

    @BeforeEach
    void ejecutarAntes() {
        LOG.warning("Ejecucion del BeforeEach");
        folio = new Folio("FOL-TEST");
        apartamento = new Apartamento("Suite Palma", 2, 4, "Jacuzzi", 100000);
        temporadas = new ArrayList<>();
    }

    @AfterEach
    void ejecutarDespues() {
        LOG.warning("Ejecucion del AfterEach");
    }

    // Verifica la liquidación noche por noche cuando una misma estancia tiene dos temporadas distintas y excluye del cobro a los ocupantes menores de 6 años a la fecha de entrada.
    @DisplayName("Prueba liquidación noche por noche con menores de 6 años gratis")
    @Test
    void calcularPrecioNochePorNocheConMenoresDeSeisAniosGratis() {
        LOG.info("Iniciando prueba calcularPrecioNochePorNocheConMenoresDeSeisAniosGratis...");
        LocalDate entrada = LocalDate.now().plusDays(2);
        LocalDate salida = LocalDate.now().plusDays(4);

        temporadas.add(new Temporada("Puente", EstadoTemporada.MEDIA,
                LocalDate.now().plusDays(3), LocalDate.now().plusDays(5)));

        ArrayList<Ocupante> ocupantes = new ArrayList<>();
        ocupantes.add(new Ocupante("Adulto 1", LocalDate.of(1990, 5, 10)));
        ocupantes.add(new Ocupante("Niño 6 años", entrada.minusYears(6)));
        ocupantes.add(new Ocupante("Niño 5 años", entrada.minusYears(5)));

        int total = folio.calcularPrecioAlojamiento(apartamento, temporadas, entrada, salida, ocupantes);

        Assertions.assertEquals(460000, total);
        LOG.info("Total liquidado: $" + total);
        LOG.info("Finalizando prueba calcularPrecioNochePorNocheConMenoresDeSeisAniosGratis...");
    }

    // Verifica la acumulación de cargos (alojamiento, servicios adicionales y penalidades),
    // el registro de abonos parciales en distintos medios de pago y el cálculo del saldo.
    @DisplayName("Prueba acumulación de cargos, pagos parciales y saldo pendiente")
    @Test
    void cargosPagosParcialesYSaldoPendiente() {
        LOG.info("Iniciando prueba cargosPagosParcialesYSaldoPendiente...");
        folio.registrarCargo(new Cargo("Alojamiento", 300000));

        ServicioAdicional desayuno = new ServicioAdicional("Desayuno", 30000);
        Penalidad dano = new Penalidad("Llave perdida", 20000);
        folio.registrarCargo(desayuno.generarCargo());
        folio.registrarCargo(dano.generarCargo());

        folio.registrarPago(new Pago(MedioPago.EFECTIVO, 100000));
        folio.registrarPago(new Pago(MedioPago.TRANSFERENCIA, 150000));

        Assertions.assertEquals(350000, folio.calcularTotalCargos());
        Assertions.assertEquals(250000, folio.calcularTotalPagos());
        Assertions.assertEquals(100000, folio.calcularSaldoPendiente());
        LOG.info("Saldo pendiente calculado: $" + folio.calcularSaldoPendiente());
        LOG.info("Finalizando prueba cargosPagosParcialesYSaldoPendiente...");
    }

    // Verifica que un error en un cargo del folio se corrija mediante un movimiento contrario
    // (valor negativo) sin eliminar ni modificar el registro original.
    @DisplayName("Prueba reverso de cargo mediante movimiento contrario")
    @Test
    void movimientoContrarioCorrigeSinEliminarCargos() {
        LOG.info("Iniciando prueba movimientoContrarioCorrigeSinEliminarCargos...");
        Cargo cargoErroneo = new Cargo("Lavandería", 40000);
        folio.registrarCargo(cargoErroneo);
        folio.registrarMovimientoContrarioCargo(cargoErroneo, "Error de digitación");

        Assertions.assertEquals(2, folio.getListCargos().size());
        Assertions.assertEquals(0, folio.calcularTotalCargos());
        LOG.info("Finalizando prueba movimientoContrarioCorrigeSinEliminarCargos...");
    }

    // Verifica que no se permitan pagos que superen el saldo pendiente ni nuevos pagos
    // cuando el saldo ya es cero, pero sí permita abonar si se agrega un nuevo cargo después.
    @DisplayName("Prueba bloqueo de pagos con saldo cero y reactivación con nuevo cargo")
    @Test
    void bloquearPagosConSaldoCeroYPermitirSiHayNuevoCargo() {
        LOG.info("Iniciando prueba bloquearPagosConSaldoCeroYPermitirSiHayNuevoCargo...");
        folio.registrarCargo(new Cargo("Alojamiento", 200000));

        Assertions.assertTrue(folio.registrarPago(new Pago(MedioPago.EFECTIVO, 150000)));
        Assertions.assertEquals(50000, folio.calcularSaldoPendiente());

        Assertions.assertFalse(folio.registrarPago(new Pago(MedioPago.EFECTIVO, 60000)));
        Assertions.assertEquals(50000, folio.calcularSaldoPendiente());

        Assertions.assertTrue(folio.registrarPago(new Pago(MedioPago.TARJETA, 50000)));
        Assertions.assertEquals(0, folio.calcularSaldoPendiente());
        Assertions.assertFalse(folio.registrarPago(new Pago(MedioPago.EFECTIVO, 10000)));

        folio.registrarCargo(new Cargo("Servicio adicional", 30000));
        Assertions.assertEquals(30000, folio.calcularSaldoPendiente());
        Assertions.assertTrue(folio.registrarPago(new Pago(MedioPago.TRANSFERENCIA, 30000)));
        Assertions.assertEquals(0, folio.calcularSaldoPendiente());
        Assertions.assertFalse(folio.registrarPago(new Pago(MedioPago.EFECTIVO, 1)));
        LOG.info("Finalizando prueba bloquearPagosConSaldoCeroYPermitirSiHayNuevoCargo...");
    }
}