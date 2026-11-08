package co.edu.uniquindio.proyectofinalprogramacion1;

public class ServicioAdicional {
    //Atributos
    private String idServicio;
    private String tipoServicio;
    private int tarifaServicio;
    //Constructor
    public ServicioAdicional(String idServicio, String tipoServicio, int tarifaServicio) {
        this.idServicio = idServicio;
        this.tipoServicio = tipoServicio;
        this.tarifaServicio = tarifaServicio;
    }
    //getter and setters
    public String getIdServicio() {
        return idServicio;
    }
    public void setIdServicio(String idServicio) {
        this.idServicio = idServicio;
    }
    public String getTipoServicio() {
        return tipoServicio;
    }
    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }
    public int getTarifaServicio() {
        return tarifaServicio;
    }
    public void setTarifaServicio(int tarifaServicio) {
        this.tarifaServicio = tarifaServicio;
    }


}

