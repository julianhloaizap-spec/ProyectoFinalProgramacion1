

public class Alojamiento {
    private String nitAlojamiento;
    private String nombreAlojamiento;
    private String direccionAlojamiento;

    public Alojamiento(String nitAlojamiento, String nombreAlojamiento, String direccionAlojamiento) {
        this.nitAlojamiento = nitAlojamiento;
        this.nombreAlojamiento = nombreAlojamiento;
        this.direccionAlojamiento = direccionAlojamiento;
    }

    public String getNitAlojamiento() {
        return nitAlojamiento;
    }

    public void setNitAlojamiento(String nitAlojamiento) {
        this.nitAlojamiento = nitAlojamiento;
    }

    public String getNombreAlojamiento() {
        return nombreAlojamiento;
    }

    public void setNombreAlojamiento(String nombreAlojamiento) {
        this.nombreAlojamiento = nombreAlojamiento;
    }

    public String getDireccionAlojamiento() {
        return direccionAlojamiento;
    }

    public void setDireccionAlojamiento(String direccionAlojamiento) {
        this.direccionAlojamiento = direccionAlojamiento;
    }
}
