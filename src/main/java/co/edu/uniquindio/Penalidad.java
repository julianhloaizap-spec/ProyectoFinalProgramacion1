package co.edu.uniquindio;

public class Penalidad implements IFacturable {
    private static int contador = 0;
    // atributos
    private final String idPenalidad;
    private final String descripcionPenalidad;
    private final int tarifaPenalidad;
    //constructor
    public Penalidad(String descripcionPenalidad, int tarifaPenalidad) {
        this.idPenalidad = String.format("PEN-%03d", ++contador);
        this.descripcionPenalidad = descripcionPenalidad;
        this.tarifaPenalidad = tarifaPenalidad;
    }

    @Override
    public Cargo generarCargo() {
        return new Cargo("Penalidad: " + descripcionPenalidad, tarifaPenalidad);
    }
    // gets
    public String getIdPenalidad() {
        return idPenalidad;
    }
    public String getDescripcionPenalidad() {
        return descripcionPenalidad;
    }
    public int getTarifaPenalidad() {
        return tarifaPenalidad;
    }
}