package co.edu.uniquindio;

public class ServicioAdicional implements IFacturable {
    private static int contador = 0;

    private final String idServicio;
    private final String tipoServicio;
    private final int tarifaServicio;
    private final int cantidad;

    public ServicioAdicional(String tipoServicio, int tarifaServicio) {
        this(tipoServicio, tarifaServicio, 1);
    }

    public ServicioAdicional(String tipoServicio, int tarifaServicio, int cantidad) {
        this.idServicio = String.format("SRV-%03d", ++contador);
        this.tipoServicio = tipoServicio != null ? tipoServicio.trim() : "";
        this.tarifaServicio = tarifaServicio;
        this.cantidad = cantidad;
    }

    public boolean esValido() {
        return !tipoServicio.isBlank() && tarifaServicio > 0 && cantidad > 0;
    }

    @Override
    public Cargo generarCargo() {
        int valorTotal = tarifaServicio * cantidad;
        String concepto = String.format("Servicio Adicional: %s (Cant: %d x $%d)",
                tipoServicio, cantidad, tarifaServicio);
        return new Cargo(concepto, valorTotal);
    }

    public String getIdServicio() {
        return idServicio;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public int getTarifaServicio() {
        return tarifaServicio;
    }

    public int getCantidad() {
        return cantidad;
    }
}
