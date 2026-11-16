package co.edu.uniquindio;

import java.time.LocalDate;

public class Bloqueo {
    private static int contador = 0;
    // atributos
    private final String idBloqueo;
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;
    private final String motivo;
    //constructor
    public Bloqueo(LocalDate fechaInicio, LocalDate fechaFin, String motivo) {
        this.idBloqueo = String.format("BLQ-%03d", ++contador);
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.motivo = motivo != null ? motivo.trim() : "";
    }
    //validaciones
    public boolean esValido() {
        return !idBloqueo.isBlank() && fechaInicio != null && fechaFin != null
                && fechaFin.isAfter(fechaInicio) && !motivo.isBlank();
    }

    public boolean solapaCon(LocalDate inicio, LocalDate fin) {
        return fechaInicio.isBefore(fin) && fechaFin.isAfter(inicio);
    }
    // getters and setters
    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Bloqueo.contador = contador;
    }

    public String getIdBloqueo() {
        return idBloqueo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public String getMotivo() {
        return motivo;
    }
}