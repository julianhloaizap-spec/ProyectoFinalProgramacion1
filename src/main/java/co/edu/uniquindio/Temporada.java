package co.edu.uniquindio;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Temporada {
    //atributos
    private final String nombreTemporada;
    private final EstadoTemporada estadoTemporada;
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;

    //Constructor
    public Temporada(String nombreTemporada, EstadoTemporada estadoTemporada,
                     LocalDate fechaInicio, LocalDate fechaFin) {
        this.nombreTemporada = nombreTemporada != null ? nombreTemporada.trim() : estadoTemporada.name();
        this.estadoTemporada = estadoTemporada;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }


    // validaciones
    public Temporada(EstadoTemporada estadoTemporada, LocalDateTime fechaInicioTemporada, LocalDateTime fechaFinTemporada) {
        this(estadoTemporada != null ? estadoTemporada.name() : "",
                estadoTemporada,
                fechaInicioTemporada != null ? fechaInicioTemporada.toLocalDate() : null,
                fechaFinTemporada != null ? fechaFinTemporada.toLocalDate() : null);
    }

    public boolean incluyeFecha(LocalDate fecha) {
        if (fecha == null || fechaInicio == null || fechaFin == null) return false;
        return !fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin);
    }
    // Los datos del constructor se revisan antes de registrar la temporada.
    public boolean esValido() {
        return estadoTemporada != null && fechaInicio != null
                && fechaFin != null && !fechaFin.isBefore(fechaInicio);
    }
    //gets
    public String getNombreTemporada() { return nombreTemporada; }
    public EstadoTemporada getEstadoTemporada() { return estadoTemporada; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
}