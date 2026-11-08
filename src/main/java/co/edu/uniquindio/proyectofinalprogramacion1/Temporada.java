package co.edu.uniquindio.proyectofinalprogramacion1;

import java.time.LocalDate;

public class Temporada {
    //atributos
 private EstadoTemporada estadoTemporada;
 private LocalDate fechaInicioTemporada;
 private LocalDate fechaFinTemporada;

    //Constructor
    public Temporada(EstadoTemporada estadoTemporada, LocalDate fechaInicioTemporada, LocalDate fechaFinTemporada) {
        this.estadoTemporada = estadoTemporada;
        this.fechaInicioTemporada = fechaInicioTemporada;
        this.fechaFinTemporada = fechaFinTemporada;
    }


    //getter and setter
    public EstadoTemporada getEstadoTemporada() {
        return estadoTemporada;
    }
    public void setEstadoTemporada(EstadoTemporada estadoTemporada) {
        this.estadoTemporada = estadoTemporada;
    }
    public LocalDate getFechaInicioTemporada() {
        return fechaInicioTemporada;
    }
    public void setFechaInicioTemporada(LocalDate fechaInicioTemporada) {
        this.fechaInicioTemporada = fechaInicioTemporada;
    }
    public LocalDate getFechaFinTemporada() {
        return fechaFinTemporada;
    }
    public void setFechaFinTemporada(LocalDate fechaFinTemporada) {
        this.fechaFinTemporada = fechaFinTemporada;
    }

    //metodos
    public int obtenerPorcentaje() {
        return estadoTemporada.getPorcentajeTemporada();
    }
    // Los datos del constructor se revisan antes de registrar la temporada.
    public boolean esValido(){
        return estadoTemporada != null
        && fechaInicioTemporada != null
        && fechaFinTemporada != null
        && fechaFinTemporada.isAfter(fechaInicioTemporada) ;
    }

}
