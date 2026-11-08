import java.time.LocalDate;
import java.util.ArrayList;

public class Recepcionista extends Usuario implements IReservable {

    private ArrayList<Reserva> listReservas;

    public Recepcionista(String nombreUsuario, String idUsuario, String correoUsuario, String telefonoUsuario, String contraseniaUsuario, ArrayList<Reserva> listReservas) {
        super(nombreUsuario, idUsuario, correoUsuario, telefonoUsuario, contraseniaUsuario);
    }

    @Override
    public Reserva registrarReserva(String idReserva, LocalDate fechaReserva, Titular infoTitular,
                                    EstadoReserva estadoReserva, LocalDate fechaEntrada, LocalDate fechaSalida) {
        return null;
    }
}
