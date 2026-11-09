import java.time.LocalDate;
import java.util.ArrayList;

public class Titular extends Usuario implements IReservable {

    //Atributos
    private LocalDate fechaNacTitular;
    private String fotoTitular;
    private ArrayList<Ocupante> listOcupantes;
    private ArrayList<Reserva> listReservas;

    //constructor
    public Titular(String nombreUsuario, String idUsuario, String correoUsuario, String telefonoUsuario,
                   String contraseniaUsuario, LocalDate fechaNacTitular, String fotoTitular, ArrayList<Ocupante> listOcupantes, ArrayList<Reserva>) {
        super(nombreUsuario, idUsuario, correoUsuario, telefonoUsuario, contraseniaUsuario);
        this.fechaNacTitular = fechaNacTitular;
        this.fotoTitular = fotoTitular;
        this.listOcupantes = new ArrayList<>();
    }

    @Override
    public boolean esValido() {
        return super.esValido();
    }

    //getters and setters
    public LocalDate getFechaNacTitular() {
        return fechaNacTitular;
    }

    public boolean setFechaNacTitular() {
        boolean validado = fechaNacTitular != null && !fechaNacTitular.plusYears(18).isAfter(LocalDate.now());
        if (validado) {
            this.fechaNacTitular = fechaNacTitular;
        }
        return validado;
    }

    public String getFotoTitular() {
        return fotoTitular;
    }

    public ArrayList<Ocupante> getListOcupantes() {
        return new ArrayList<>(listOcupantes);
    }


    @Override
    public Reserva registrarReserva(String idReserva, LocalDate fechaReserva, Titular infoTitular,
                                    EstadoReserva estadoReserva, LocalDate fechaEntrada, LocalDate fechaSalida) {
        return null;
    }
}
