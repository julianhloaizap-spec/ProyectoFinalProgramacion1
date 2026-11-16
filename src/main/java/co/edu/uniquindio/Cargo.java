package co.edu.uniquindio;

import java.time.LocalDateTime;

public class Cargo {
    private static int contador = 0;

    private final String idCargo;
    private final String concepto;
    private final int valorCargo;
    private final LocalDateTime fechaCargo;

    public Cargo(String concepto, int valorCargo) {
        this.idCargo = String.format("CARG-%03d", ++contador);
        this.concepto = concepto;
        this.valorCargo = valorCargo;
        this.fechaCargo = LocalDateTime.now();
    }

    public String getIdCargo() { return idCargo; }
    public String getConcepto() { return concepto; }
    public int getValorCargo() { return valorCargo; }
    public LocalDateTime getFechaCargo() { return fechaCargo; }
}