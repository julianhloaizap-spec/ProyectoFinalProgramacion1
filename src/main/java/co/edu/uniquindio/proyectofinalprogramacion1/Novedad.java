package co.edu.uniquindio.proyectofinalprogramacion1;

import java.time.LocalDateTime;

public class Novedad {
    private static int contador = 0;
    // atributos
    private final String idNovedad;
    private final String descripcion;
    private final String registradoPor;
    private final LocalDateTime fechaRegistro;
    //constructor
    public Novedad(String descripcion, String registradoPor) {
        this.idNovedad = String.format("NOV-%03d", ++contador);
        this.descripcion = descripcion;
        this.registradoPor = registradoPor;
        this.fechaRegistro = LocalDateTime.now();
    }
    //getters and setters
    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Novedad.contador = contador;
    }

    public String getIdNovedad() {
        return idNovedad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getRegistradoPor() {
        return registradoPor;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
}
