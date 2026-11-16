package co.edu.uniquindio;

import java.time.LocalDateTime;

public class Novedad {
    private static int contador = 0;

    private final String idNovedad;
    private final String descripcion;
    private final String registradoPor;
    private final LocalDateTime fechaRegistro;

    private boolean solucionada;
    private String solucion;
    private String solucionadoPor;
    private LocalDateTime fechaSolucion;

    public Novedad(String descripcion, String registradoPor) {
        this.idNovedad = String.format("NOV-%03d", ++contador);
        this.descripcion = descripcion != null ? descripcion.trim() : "";
        this.registradoPor = registradoPor != null ? registradoPor.trim() : "";
        this.fechaRegistro = LocalDateTime.now();
        this.solucionada = false;
        this.solucion = "";
        this.solucionadoPor = "";
        this.fechaSolucion = null;
    }

    public boolean registrarSolucion(String solucion, String solucionadoPor) {
        if (solucion == null || solucion.isBlank()) {
            return false;
        }
        this.solucion = solucion.trim();
        this.solucionadoPor = (solucionadoPor != null && !solucionadoPor.isBlank())
                ? solucionadoPor.trim() : "Administrador";
        this.fechaSolucion = LocalDateTime.now();
        this.solucionada = true;
        return true;
    }

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

    public boolean isSolucionada() {
        return solucionada;
    }

    public String getSolucion() {
        return solucion;
    }

    public String getSolucionadoPor() {
        return solucionadoPor;
    }

    public LocalDateTime getFechaSolucion() {
        return fechaSolucion;
    }
}
