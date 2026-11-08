import java.time.LocalDate;

public interface IReservable {
    public Reserva registrarReserva(String idReserva, LocalDate fechaReserva, Titular infoTitular,
                                    EstadoReserva estadoReserva, LocalDate fechaEntrada, LocalDate fechaSalida);
}
