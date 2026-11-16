package co.edu.uniquindio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Reserva {
    private static int contador = 0;

    private final String idReserva;
    private final LocalDateTime fechaReserva;
    private final Titular infoTitular;
    private final Apartamento apartamento;
    private final ArrayList<Ocupante> listOcupantes;
    private EstadoReserva estadoReserva;
    private final LocalDate fechaEntrada;
    private final LocalDate fechaSalida;
    private final Canal canal;
    private final String codigoExterno;
    private final Folio folio;
    private int valorTotalCongelado;
    private int comisionPlataforma;
    private PoliticaCancelacion politicaCongelada;
    private int valorDevueltoCancelacion;

    public Reserva(Titular infoTitular, Apartamento apartamento,
                   ArrayList<Ocupante> ocupantes, LocalDate fechaEntrada, LocalDate fechaSalida,
                   Canal canal, String codigoExterno) {
        this.idReserva = String.format("RES-%03d", ++contador);
        this.fechaReserva = LocalDateTime.now();
        this.infoTitular = infoTitular;
        this.apartamento = apartamento;
        this.listOcupantes = ocupantes != null ? new ArrayList<>(ocupantes) : new ArrayList<>();
        this.estadoReserva = EstadoReserva.PENDIENTE;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.canal = canal;
        this.codigoExterno = codigoExterno != null ? codigoExterno.trim() : "";
        this.folio = new Folio("FOL-" + this.idReserva);
        this.valorTotalCongelado = 0;
        this.comisionPlataforma = 0;
        this.politicaCongelada = new PoliticaCancelacion();
        this.valorDevueltoCancelacion = 0;
    }

    public boolean solapaCon(LocalDate inicio, LocalDate fin) {
        return fechaEntrada.isBefore(fin) && fechaSalida.isAfter(inicio);
    }

    public boolean cambiarEstado(EstadoReserva nuevoEstado) {
        if (this.estadoReserva == nuevoEstado) return true;

        switch (this.estadoReserva) {
            case PENDIENTE:
                if (nuevoEstado == EstadoReserva.CONFIRMADA || nuevoEstado == EstadoReserva.CANCELADA) {
                    this.estadoReserva = nuevoEstado;
                    return true;
                }
                break;
            case CONFIRMADA:
                if (nuevoEstado == EstadoReserva.EN_CURSO || nuevoEstado == EstadoReserva.CANCELADA || nuevoEstado == EstadoReserva.NO_SHOW) {
                    this.estadoReserva = nuevoEstado;
                    return true;
                }
                break;
            case EN_CURSO:
                if (nuevoEstado == EstadoReserva.FINALIZADA) {
                    this.estadoReserva = nuevoEstado;
                    return true;
                }
                break;
            case FINALIZADA:
            case CANCELADA:
            case NO_SHOW:
                return false;
        }
        return false;
    }

    public void liquidarYCongelarValor(ArrayList<Temporada> temporadas, PoliticaCancelacion politicaActual) {
        int totalEstancia = folio.calcularPrecioAlojamiento(apartamento, temporadas, fechaEntrada, fechaSalida, listOcupantes);
        this.valorTotalCongelado = totalEstancia;
        if (politicaActual != null) {
            this.politicaCongelada = politicaActual.clonar();
        }
        if (this.canal == Canal.EXTERNA) {
            this.comisionPlataforma = (int) Math.round(totalEstancia * 0.15);
        }
        this.folio.registrarCargo(new Cargo("Valor Estancia Congelado", totalEstancia));
    }

    public int previsualizarDevolucionCancelacion(LocalDate fechaCancelacion) {
        if (estadoReserva != EstadoReserva.PENDIENTE && estadoReserva != EstadoReserva.CONFIRMADA) {
            return 0;
        }
        if (fechaCancelacion == null || politicaCongelada == null) {
            return 0;
        }
        int totalPagado = folio.calcularTotalPagos();
        if (totalPagado <= 0) {
            return 0;
        }
        return politicaCongelada.calcularDevolucion(valorTotalCongelado, totalPagado, fechaCancelacion, fechaEntrada);
    }

    public boolean cancelar(LocalDate fechaCancelacion) {
        if (estadoReserva != EstadoReserva.PENDIENTE && estadoReserva != EstadoReserva.CONFIRMADA) {
            return false;
        }
        int devolucion = previsualizarDevolucionCancelacion(fechaCancelacion);
        if (cambiarEstado(EstadoReserva.CANCELADA)) {
            this.valorDevueltoCancelacion = devolucion;
            return true;
        }
        return false;
    }

    public int calcularAnticipoMinimoPortal() {
        if (canal != Canal.PORTAL) return 0;
        return (int) Math.ceil(valorTotalCongelado * 0.30);
    }

    public String getIdReserva() { return idReserva; }
    public LocalDateTime getFechaReserva() { return fechaReserva; }
    public Titular getInfoTitular() { return infoTitular; }
    public Apartamento getApartamento() { return apartamento; }
    public ArrayList<Ocupante> getListOcupantes() { return new ArrayList<>(listOcupantes); }
    public EstadoReserva getEstadoReserva() { return estadoReserva; }
    public LocalDate getFechaEntrada() { return fechaEntrada; }
    public LocalDate getFechaSalida() { return fechaSalida; }
    public Canal getCanal() { return canal; }
    public Folio getFolio() { return folio; }
    public int getValorTotalCongelado() { return valorTotalCongelado; }
    public String getCodigoExterno() { return codigoExterno; }
    public int getComisionPlataforma() { return comisionPlataforma; }
    public PoliticaCancelacion getPoliticaCongelada() { return politicaCongelada; }
    public int getValorDevueltoCancelacion() { return valorDevueltoCancelacion; }
}