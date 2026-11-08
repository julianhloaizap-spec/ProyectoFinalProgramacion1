import java.time.LocalDate;

public class Reserva {
    private String idReserva;
    private LocalDate fechaReserva;
    private Titular infoTitular;
    private EstadoReserva estadoReserva;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;

    private Folio folio;

    public Reserva(String idReserva, LocalDate fechaReserva, Titular infoTitular, EstadoReserva estadoReserva, LocalDate fechaEntrada, LocalDate fechaSalida, Folio folio) {
        this.idReserva = idReserva;
        this.fechaReserva = fechaReserva;
        this.infoTitular = infoTitular;
        this.estadoReserva = estadoReserva;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.folio = folio;
    }

    public String getIdReserva() {
        return idReserva;
    }

    public LocalDate getFechaReserva() {return fechaReserva;}

    public boolean setFechaReserva(LocalDate fechaReserva) {


        this.fechaReserva = fechaReserva;
    }

    public Titular getInfoTitular() {
        return infoTitular;
    }

    public EstadoReserva getEstadoReserva() {
        return estadoReserva;
    }

    public boolean setEstadoReserva(EstadoReserva estadoReserva) {
        boolean validado = estadoReserva != null;
        if (estadoReserva != EstadoReserva.EN_CURSO) {
            this.estadoReserva = estadoReserva;
        }
        return validado;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public boolean setFechaSalida(LocalDate fechaSalida, LocalDate fechaEntrada) {
        boolean validado= fechaSalida != null && fechaEntrada != null && fechaSalida.isAfter(this.fechaEntrada);
        if (validado) {
            this.fechaSalida = fechaSalida;
        }

    }

    public static int saldoPagar (Folio folio) {

    }
}
