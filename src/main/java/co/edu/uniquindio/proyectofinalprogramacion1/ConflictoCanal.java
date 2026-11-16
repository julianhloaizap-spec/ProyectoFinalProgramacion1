package co.edu.uniquindio.proyectofinalprogramacion1;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ConflictoCanal {
    private static int contador = 0;
    // atributos
    private final String idConflicto;
    private final String codigoExterno;
    private final String idApartamento;
    private final LocalDate fechaEntrada;
    private final LocalDate fechaSalida;
    private final String motivo;
    private final LocalDateTime fechaRegistro;
    //constructor
    public ConflictoCanal(String codigoExterno, String idApartamento,
                          LocalDate fechaEntrada, LocalDate fechaSalida, String motivo) {
        this.idConflicto = String.format("CONF-%03d", ++contador);
        this.codigoExterno = codigoExterno;
        this.idApartamento = idApartamento;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.motivo = motivo;
        this.fechaRegistro = LocalDateTime.now();
    }
    // getters and setters
    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        ConflictoCanal.contador = contador;
    }

    public String getIdConflicto() {
        return idConflicto;
    }

    public String getCodigoExterno() {
        return codigoExterno;
    }

    public String getIdApartamento() {
        return idApartamento;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public String getMotivo() {
        return motivo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
}