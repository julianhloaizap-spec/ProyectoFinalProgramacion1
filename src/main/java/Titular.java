package co.edu.uniquindio;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

public class Titular extends Usuario implements IReservable {
    private LocalDate fechaNacTitular;
    private String fotoTitular;
    private final ArrayList<Ocupante> listOcupantes;
    private final ArrayList<Reserva> listReservas;

    public Titular(String nombreUsuario, String idUsuario, String correoUsuario, String telefonoUsuario,
                   String contraseniaUsuario, LocalDate fechaNacTitular, String fotoTitular) {
        super(nombreUsuario, idUsuario, correoUsuario, telefonoUsuario, contraseniaUsuario);
        this.fechaNacTitular = fechaNacTitular;
        this.fotoTitular = fotoTitular != null ? fotoTitular : "";
        this.listOcupantes = new ArrayList<>();
        this.listReservas = new ArrayList<>();
    }

    @Override
    public String getRol() {
        return "HUESPED";
    }

    @Override
    public boolean esValido() {
        if (!super.esValido() || fechaNacTitular == null) return false;
        return Period.between(fechaNacTitular, LocalDate.now()).getYears() >= 18;
    }

    public void agregarOcupante(Ocupante ocupante) {
        if (ocupante != null && ocupante.esValido()) {
            listOcupantes.add(ocupante);
        }
    }

    public void limpiarOcupantes() {
        listOcupantes.clear();
    }

    @Override
    public Reserva registrarReserva(Apartamento apartamento, Titular infoTitular,
                                    ArrayList<Ocupante> ocupantes, LocalDate fechaEntrada,
                                    LocalDate fechaSalida, Canal canal, String codigoExterno) {
        Reserva reserva = new Reserva(infoTitular, apartamento, ocupantes, fechaEntrada, fechaSalida, canal, codigoExterno);
        listReservas.add(reserva);
        return reserva;
    }

    public void asociarReserva(Reserva reserva) {
        if (reserva != null && !listReservas.contains(reserva)) {
            listReservas.add(reserva);
        }
    }

    public LocalDate getFechaNacTitular() { return fechaNacTitular; }
    public void setFechaNacTitular(LocalDate fechaNacTitular) { this.fechaNacTitular = fechaNacTitular; }
    public String getFotoTitular() { return fotoTitular; }
    public void setFotoTitular(String fotoTitular) { this.fotoTitular = fotoTitular != null ? fotoTitular : ""; }
    public ArrayList<Ocupante> getListOcupantes() { return new ArrayList<>(listOcupantes); }
    public ArrayList<Reserva> getListReservas() { return new ArrayList<>(listReservas); }
}