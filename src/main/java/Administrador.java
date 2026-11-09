import java.util.ArrayList;

public class Administrador extends Usuario {
    private ArrayList<Recepcionista> listRecepcionistas;
    private ArrayList<Apartamento> listApartamentos;
    private ArrayList<Temporada> listTemporadas;

    // Consructor
    public Administrador(String nombreUsuario, String idUsuario, String correoUsuario, String telefonoUsuario, String contraseniaUsuario,
                         ArrayList<Recepcionista> listRecepcionistas, ArrayList<Apartamento> listApartamentos, ArrayList<Temporada> listTemporadas) {
        super(nombreUsuario, idUsuario, correoUsuario, telefonoUsuario, contraseniaUsuario);
        this.listRecepcionistas = new ArrayList<>();
        this.listApartamentos = new ArrayList<>();
        this.listTemporadas = new ArrayList<>();
    }

    @Override
    public boolean esValido() {
        return super.esValido();
    }

    public ArrayList<Recepcionista> getListRecepcionistas() {
        return new ArrayList<>(listRecepcionistas);
    }

    public ArrayList<Apartamento> getListApartamentos() {
        return new ArrayList<>(listApartamentos);
    }

    public ArrayList<Temporada> getListTemporadas() {
        return new ArrayList<>(listTemporadas);
    }

    public boolean crearRecepcionista(Recepcionista recepcionista) {
        boolean registrado = false;
        if (recepcionista != null && recepcionista.esValido() && buscarRecepcionista(recepcionista.getIdUsuario()) == null) {
            listRecepcionistas.add(recepcionista);
            registrado = true;
        }
        return registrado;
    }

    public Recepcionista buscarRecepcionista(String idRecepcionista) {
        Recepcionista encontrado = null;
        if (idRecepcionista != null) {
            for (Recepcionista recepcionista : listRecepcionistas) {
                if (recepcionista.getIdUsuario().equals(idRecepcionista.trim())) {
                    encontrado = recepcionista;
                }
            }
        }
        return encontrado;
    }

    public boolean actualizarRecepcionista(String idRecepcionista, String nombreNuevo, String correoNuevo, String telefonoNuevo, String contraseniaNueva) {
        boolean actualizado = false;
        Recepcionista recepcionista = buscarRecepcionista(idRecepcionista);
        if (recepcionista != null) {
            recepcionista.setNombreUsuario(nombreNuevo);
            recepcionista.setCorreoUsuario(correoNuevo);
            recepcionista.setTelefonoUsuario(telefonoNuevo);
            recepcionista.setContraseniaUsuario(contraseniaNueva);
            actualizado = true;
        }
        return actualizado;
    }

    public boolean eliminarRecepcionista(String idRecepcionista) {
        boolean eliminado = false;
        Recepcionista recepcionista = buscarRecepcionista(idRecepcionista);
        if (recepcionista != null && recepcionista) {
            listRecepcionistas.remove(recepcionista);
            eliminado = true;
        }
        return eliminado;

    }

}
