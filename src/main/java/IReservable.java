package co.edu.uniquindio;

import java.time.LocalDate;
import java.util.ArrayList;

public interface IReservable {
    Reserva registrarReserva(Apartamento apartamento, Titular infoTitular,
                             ArrayList<Ocupante> ocupantes, LocalDate fechaEntrada,
                             LocalDate fechaSalida, Canal canal, String codigoExterno);
}