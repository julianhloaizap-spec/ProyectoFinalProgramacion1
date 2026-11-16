package co.edu.uniquindio;

public class ServicioAdicional implements IFacturable {
    private static int contador = 0;
    //atributos
    private final String idServicio;
    private final String tipoServicio;
    private final int tarifaServicio;
    // constructor
    public ServicioAdicional(String tipoServicio, int tarifaServicio) {
        this.idServicio = String.format("SRV-%03d", ++contador);
        this.tipoServicio = tipoServicio;
        this.tarifaServicio = tarifaServicio;
    }

    @Override
    public Cargo generarCargo() {
        return new Cargo("Servicio Adicional: " + tipoServicio, tarifaServicio);
    }
    //gets
    public String getIdServicio() {
        return idServicio;
    }
    public String getTipoServicio() {
        return tipoServicio;
    }
    public int getTarifaServicio() {
        return tarifaServicio;
    }
}
