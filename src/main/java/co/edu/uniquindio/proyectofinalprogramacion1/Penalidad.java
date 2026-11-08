package co.edu.uniquindio.proyectofinalprogramacion1;

public class Penalidad {
    //atributos
    private String idPenalidad;
    private String descripcionPenalidad;
    private int tarifaPenalidad;
    //Constructor
    public Penalidad(String idPenalidad, String descripcionPenalidad, int tarifaPenalidad) {
        this.idPenalidad = idPenalidad;
        this.descripcionPenalidad = descripcionPenalidad;
        this.tarifaPenalidad = tarifaPenalidad;
    }
    //getters and setters
    public String getIdPenalidad() {
        return idPenalidad;
    }
    public void setIdPenalidad(String idPenalidad) {
        this.idPenalidad = idPenalidad;
    }
    public String getDescripcionPenalidad() {
        return descripcionPenalidad;
    }
    public void setDescripcionPenalidad(String descripcionPenalidad) {
        this.descripcionPenalidad = descripcionPenalidad;
    }
    public int getTarifaPenalidad() {
        return tarifaPenalidad;
    }
    public void setTarifaPenalidad(int tarifaPenalidad) {
        this.tarifaPenalidad = tarifaPenalidad;
    }

}
