import java.util.ArrayList;

public class Alojamiento {
    private String nitAlojamiento;
    private String nombreAlojamiento;
    private String direccionAlojamiento;

    ArrayList<Usuario> listUsuarios;
    ArrayList<Administrador> listAdministrador;
    ArrayList<Apartamento> listApartamentos;

    public Alojamiento(String nitAlojamiento, String nombreAlojamiento, String direccionAlojamiento,
                       ArrayList<Usuario> listUsuarios, ArrayList<Apartamento> listApartamentos) {

        if (nitAlojamiento == null) {
            this.nitAlojamiento = "";
        } else {
            this.nitAlojamiento.trim();
        }
        this.nombreAlojamiento = nombreAlojamiento;
        this.direccionAlojamiento = direccionAlojamiento;
        this.listUsuarios = new ArrayList<>();
        this.listApartamentos = new ArrayList<>();
    }

        // los datos del constructor se revisan antes de crear un alojamiento.

        public boolean esValido() {
            return nitAlojamiento != null && !nitAlojamiento.isBlank()
                    && nombreAlojamiento != null && !nombreAlojamiento.isBlank()
                    && direccionAlojamiento != null && !direccionAlojamiento.isBlank();
        }

    public String getNitAlojamiento() {return nitAlojamiento;}

    public boolean setNitAlojamiento(String nitAlojamiento) {
        boolean actualizado = nitAlojamiento != null && !nitAlojamiento.isBlank();
        if (actualizado) {
            this.nitAlojamiento = nitAlojamiento.trim();
        }
        return actualizado;
    }

    public String getNombreAlojamiento() {
        return nombreAlojamiento;
    }

    public boolean setNombreAlojamiento(String nombreAlojamiento) {
        boolean actualizado = nombreAlojamiento != null && !nombreAlojamiento.isBlank();
        if(actualizado) {
            this.nombreAlojamiento = nombreAlojamiento.trim();
        }
        return actualizado;
    }

    public String getDireccionAlojamiento() {
        return direccionAlojamiento;
    }

    public boolean setDireccionAlojamiento(String direccionAlojamiento) {
        boolean actualizado = direccionAlojamiento != null && !direccionAlojamiento.isBlank();
        if(actualizado) {
            this.direccionAlojamiento = direccionAlojamiento.trim();
        }
        return actualizado;
    }

    public ArrayList<Usuario> getListUsuarios() {
        return new ArrayList<> (listUsuarios);
    }

    public ArrayList<Apartamento> getListApartamentos() {
        return new ArrayList<>(listApartamentos);
    }

    public ArrayList<Administrador> getlistAdministrador() {
        return new ArrayList<> (listAdministrador);
    }

    public boolean registrarAdministrador (Administrador administrador) {


}




}
