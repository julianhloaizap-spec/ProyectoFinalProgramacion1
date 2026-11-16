package co.edu.uniquindio;

import java.time.LocalDate;
import java.util.ArrayList;

public class Recepcionista extends Usuario implements IReservable {
    private final ArrayList<Reserva> listReservasGestionadas;

    public Recepcionista(String nombreUsuario, String idUsuario, String correoUsuario,
                         String telefonoUsuario, String contraseniaUsuario) {
        super(nombreUsuario, idUsuario, correoUsuario, telefonoUsuario, contraseniaUsuario);
        this.listReservasGestionadas = new ArrayList<>();
    }

    @Override
    public String getRol() {
        return "RECEPCIONISTA";
    }

    @Override
    public Reserva registrarReserva(Apartamento apartamento, Titular infoTitular,
                                    ArrayList<Ocupante> ocupantes, LocalDate fechaEntrada,
                                    LocalDate fechaSalida, Canal canal, String codigoExterno) {
        Reserva r = new Reserva(infoTitular, apartamento, ocupantes, fechaEntrada, fechaSalida, canal, codigoExterno);
        listReservasGestionadas.add(r);
        infoTitular.asociarReserva(r);
        return r;
    }

    public ArrayList<Reserva> getListReservasGestionadas() {
        return new ArrayList<>(listReservasGestionadas);
    }
}