package co.edu.uniquindio;

public class Administrador extends Usuario {

    public Administrador(String nombreUsuario, String idUsuario, String correoUsuario,
                         String telefonoUsuario, String contraseniaUsuario) {
        super(nombreUsuario, idUsuario, correoUsuario, telefonoUsuario, contraseniaUsuario);
    }

    @Override
    public String getRol() {
        return "ADMINISTRADOR";
    }

    public boolean contratarRecepcionista(Alojamiento alojamiento, Recepcionista recepcionista) {
        return alojamiento != null && alojamiento.registrarUsuario(recepcionista);
    }

    public boolean editarRecepcionista(Alojamiento alojamiento, String idRecepcionista, String nombre,
                                       String correo, String telefono, String contrasenia) {
        return alojamiento != null && alojamiento.actualizarRecepcionista(idRecepcionista, nombre, correo, telefono, contrasenia);
    }

    public boolean desvincularRecepcionista(Alojamiento alojamiento, String idRecepcionista) {
        return alojamiento != null && alojamiento.desvincularRecepcionista(idRecepcionista);
    }

    public boolean crearApartamento(Alojamiento alojamiento, Apartamento apartamento) {
        return alojamiento != null && alojamiento.registrarApartamento(apartamento);
    }

    public boolean eliminarApartamentoLogico(Alojamiento alojamiento, String idApartamento) {
        return alojamiento != null && alojamiento.eliminarApartamentoLogico(idApartamento);
    }
}