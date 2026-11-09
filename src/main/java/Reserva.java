import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reserva {
    private String idReserva;
    private final LocalDateTime fechaReserva;
    private Titular infoTitular;
    private EstadoReserva estadoReserva;
    private LocalDateTime fechaEntrada;
    private LocalDateTime fechaSalida;

    private Folio folio;

    public Reserva(String idReserva, LocalDateTime fechaReserva, Titular infoTitular, EstadoReserva estadoReserva,
                   LocalDateTime fechaEntrada, LocalDateTime fechaSalida, Folio folio) {
        this.idReserva = idReserva;
        this.fechaReserva = fechaReserva;
        this.infoTitular = infoTitular;
        this.estadoReserva = EstadoReserva.PENDIENTE;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.folio = folio;
    }

    public String getIdReserva() {
        return idReserva;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
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

    public LocalDateTime getFechaEntrada() {
        return fechaEntrada;
    }

    public boolean setFechaEntrada(LocalDateTime fechaEntrada) {
        boolean validado = fechaEntrada != null && fechaEntrada.isAfter(this.fechaReserva);
        if (validado) {
            this.fechaEntrada = fechaEntrada;
        }
        return validado;
    }

    public LocalDateTime getFechaSalida() {
        return fechaSalida;
    }

    public boolean setFechaSalida(LocalDateTime fechaSalida, LocalDateTime fechaEntrada) {
        boolean validado = fechaSalida != null && fechaEntrada != null && fechaSalida.isAfter(this.fechaEntrada);
        if (validado) {
            this.fechaSalida = fechaSalida;
        }

    }

    public static int saldoPagar(Folio folio) {

    }
}
