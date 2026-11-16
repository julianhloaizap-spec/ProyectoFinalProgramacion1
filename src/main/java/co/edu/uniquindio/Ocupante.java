package co.edu.uniquindio;

import java.time.LocalDate;
import java.time.Period;

public class Ocupante {
    private final String nombreOcupante;
    private final LocalDate fechaNacOcupante;

    public Ocupante(String nombreOcupante, LocalDate fechaNacOcupante) {
        this.nombreOcupante = nombreOcupante != null ? nombreOcupante.trim() : "";
        this.fechaNacOcupante = fechaNacOcupante;
    }

    public boolean esValido() {
        return !nombreOcupante.isBlank() && fechaNacOcupante != null
                && !fechaNacOcupante.isAfter(LocalDate.now());
    }

    public int calcularEdad(LocalDate fechaReferencia) {
        if (fechaNacOcupante == null || fechaReferencia == null) return 0;
        return Period.between(fechaNacOcupante, fechaReferencia).getYears();
    }

    public boolean pagaTarifa(LocalDate fechaEntrada) {
        return calcularEdad(fechaEntrada) >= 6;
    }

    public String getNombreOcupante() { return nombreOcupante; }
    public LocalDate getFechaNacOcupante() { return fechaNacOcupante; }
}