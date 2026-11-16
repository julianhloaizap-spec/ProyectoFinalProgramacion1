package co.edu.uniquindio;

import java.time.LocalDate;
import java.util.ArrayList;

public class Folio {
    //atributos
    private final String idFolio;
    private final ArrayList<Cargo> listCargos;
    private final ArrayList<Pago> listPagos;

    //constructor
    public Folio(String idFolio) {
        this.idFolio = idFolio != null ? idFolio.trim() : "";
        this.listCargos = new ArrayList<>();
        this.listPagos = new ArrayList<>();
    }

    public static EstadoTemporada determinarTemporadaFecha(LocalDate fecha, ArrayList<Temporada> temporadas) {
        if (temporadas != null) {
            for (Temporada t : temporadas) {
                if (t != null && t.esValido() && t.incluyeFecha(fecha)) {
                    return t.getEstadoTemporada();
                }
            }
        }
        return EstadoTemporada.BAJA;
    }

    public static String generarDesgloseCotizacion(Apartamento apartamento, ArrayList<Temporada> temporadas,
                                                   LocalDate fechaEntrada, LocalDate fechaSalida,
                                                   ArrayList<Ocupante> listOcupantes) {
        if (apartamento == null || fechaEntrada == null || fechaSalida == null
                || listOcupantes == null || !fechaSalida.isAfter(fechaEntrada)) {
            return "Datos inválidos para cotizar.";
        }

        int ocupantesQuePagan = 0;
        int ocupantesMenores = 0;
        for (Ocupante o : listOcupantes) {
            if (o != null) {
                if (o.pagaTarifa(fechaEntrada)) ocupantesQuePagan++;
                else ocupantesMenores++;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Cotización Apartamento: ").append(apartamento.getNombreApartamento())
                .append(" (").append(apartamento.getIdApartamento()).append(")\n");
        sb.append("Ocupantes totales: ").append(listOcupantes.size())
                .append(" (Pagan: ").append(ocupantesQuePagan)
                .append(" | Menores < 6 años gratis: ").append(ocupantesMenores).append(")\n");
        sb.append("-------------------------------------------------------------\n");

        int total = 0;
        LocalDate fechaActual = fechaEntrada;
        while (fechaActual.isBefore(fechaSalida)) {
            EstadoTemporada temp = determinarTemporadaFecha(fechaActual, temporadas);
            int tarifaNochePersona = apartamento.getTarifaPorTemporada(temp);
            int subtotalNoche = tarifaNochePersona * ocupantesQuePagan;
            total += subtotalNoche;

            sb.append(String.format("Noche %s | Temp: %-5s | Tarifa/Pers: $%d | Subtotal: $%d COP\n",
                    fechaActual, temp, tarifaNochePersona, subtotalNoche));
            fechaActual = fechaActual.plusDays(1);
        }

        sb.append("-------------------------------------------------------------\n");
        sb.append(String.format("TOTAL ESTANCIA: $%d COP\n", total));
        return sb.toString();
    }

    public int calcularPrecioAlojamiento(Apartamento apartamento, ArrayList<Temporada> temporadas,
                                         LocalDate fechaEntrada, LocalDate fechaSalida,
                                         ArrayList<Ocupante> listOcupantes) {
        if (apartamento == null || fechaEntrada == null || fechaSalida == null
                || listOcupantes == null || !fechaSalida.isAfter(fechaEntrada)) {
            return 0;
        }

        int cantidadOcupantesQuePagan = 0;
        for (Ocupante o : listOcupantes) {
            if (o != null && o.pagaTarifa(fechaEntrada)) {
                cantidadOcupantesQuePagan++;
            }
        }

        int precioAlojamientoTotal = 0;
        LocalDate fechaActual = fechaEntrada;

        while (fechaActual.isBefore(fechaSalida)) {
            EstadoTemporada temp = determinarTemporadaFecha(fechaActual, temporadas);
            int tarifaTemporadaNoche = apartamento.getTarifaPorTemporada(temp);
            precioAlojamientoTotal += tarifaTemporadaNoche * cantidadOcupantesQuePagan;
            fechaActual = fechaActual.plusDays(1);
        }

        return precioAlojamientoTotal;
    }

    public void registrarCargo(Cargo cargo) {
        if (cargo != null && cargo.getValorCargo() != 0) {
            listCargos.add(cargo);
        }
    }

    public void registrarMovimientoContrarioCargo(Cargo cargoOriginal, String motivo) {
        if (cargoOriginal != null) {
            listCargos.add(new Cargo("Reverso (" + motivo + "): " + cargoOriginal.getConcepto(),
                    -cargoOriginal.getValorCargo()));
        }
    }

    public void registrarPago(Pago pago) {
        if (pago != null && pago.valorPago() != 0) {
            listPagos.add(pago);
        }
    }

    public int calcularTotalCargos() {
        int total = 0;
        for (Cargo c : listCargos) total += c.getValorCargo();
        return total;
    }

    public int calcularTotalPagos() {
        int total = 0;
        for (Pago p : listPagos) total += p.valorPago();
        return total;
    }

    public int calcularSaldoPendiente() {
        return calcularTotalCargos() - calcularTotalPagos();
    }

    // getters and setters
    public String getIdFolio() { return idFolio; }
    public ArrayList<Cargo> getListCargos() { return new ArrayList<>(listCargos); }
    public ArrayList<Pago> getListPagos() { return new ArrayList<>(listPagos); }
}